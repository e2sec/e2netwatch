#!/bin/bash
#
# Name: Mysql patch test
#
# Author: Hrvoje Zeljko
#
# Date: 10-07-2017
#
# Purpose: Checking if patch apply executed properly
#
# Notes: -
#
# Usage: mysqlpatchtest.sh [-p mysqlrootpassword]
#
####################################################################################################
#
# Include convenience functions.
#
. ./ci_scripts/common
#
# Check if Mysql is up and running
#
mysql_check() {
    docker exec -t kyn_mysqlpatchtest_1 mysql -e "select 1"
}
#
# Wait for Mysql to start
#
wait_mysql_start() {
    RETRY=2
    SAFETY_CHECK_MAX=5
    SAFETY_CHECK_CURRENT=$SAFETY_CHECK_MAX
    LOG=$( mysql_check > /dev/null )
    EXIT_CODE=$?
    while [ $EXIT_CODE -ne 0 ] && [ $SAFETY_CHECK_CURRENT -gt 0 ]; do
        if [ $EXIT_CODE -ne 0 ]; then
            SAFETY_CHECK_CURRENT=$((SAFETY_CHECK_CURRENT-1))
        else
            SAFETY_CHECK_CURRENT=$SAFETY_CHECK_MAX
        fi
        echo -n "$EXIT_CODE"
        sleep $RETRY
        LOG=$( mysql_check > /dev/null )
        EXIT_CODE=$?
    done
    log_success $EXIT_CODE "$LOG"
}
#
# Try
#
add_database() {
    docker exec -t $CONTAINER_NAME sh -c "mysql < ./tmp/scripts/aql.sql" > /dev/null
}
#
# Stop and remove running container
#
remove_container() {
    echo -n "Stopping and removing container..."
    LOG=$( docker stop $CONTAINER_NAME;docker rm -v $CONTAINER_NAME > /dev/null)
    log_success $? "$LOG"
}
#
# Getting the Mysql root password
#
PASSWORD="kyn_RO0T_password" #TODO
while getopts p: OPT; do
	[ $OPT == "p" ] && PASSWORD=$OPTARG
done
shift $((OPTIND-1))
#
# Setting variables
#
IMAGE="kyn/mysqlpatchtest"
#
CONTAINER_NAME="kyn_mysqlpatchtest_1"
#
KYN_NETWORK=$(docker network list --filter name=kyn* --format {{.Name}})
#
if [ ! -z "$KYN_NETWORK" ]; then
    NETWORK="--network=$KYN_NETWORK"
fi
#
# Create and start container for testing mysql patch
#
echo -n "Starting mysql patch test container..."
LOG=$( docker run -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -d --name=$CONTAINER_NAME $NETWORK kyn/mysqltest > /dev/null )
log_success $? "$LOG"
#
# Wait Mysql to start
#
echo -n "Waiting for mysql to start."
wait_mysql_start
#
# Check differences between two databases - patched database and standalone installed here
#
echo "Checking differences between databases..."
MYOUTPUT=$(docker exec -t $CONTAINER_NAME sh -c "mysqldiff --skip-table-options --force --server1=root:'$PASSWORD'@mysql --server2=root@localhost aql_db:aql_db")
EXIT_CODE=$?
echo "exit code: $EXIT_CODE"
if [ $EXIT_CODE -ne 0 ]; then
    echo "ide output: $MYOUTPUT"
    remove_container
    exit $EXIT_CODE
fi
DIFFERENCES=$(echo "$MYOUTPUT" | grep -E "^# WARNING: Objects in server|^# Comparing .*\[FAIL\]" -c)
if [ $DIFFERENCES -eq 0 ]; then
    echo "SUCCESS No differences"
else
    echo "FAIL There are $DIFFERENCES differences"
    echo "$MYOUTPUT"
fi
# Stop and remove container
remove_container
