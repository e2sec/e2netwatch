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
# Usage: mysqlpatchtest.sh
#
####################################################################################################
#
# Include convenience functions.
#
. ./ci_scripts/functions
#
# Try
#
add_database() {
    docker exec -t $CONTAINER_NAME sh -c "mysql < ./tmp/scripts/aql.sql" > /dev/null
}
# print command line option onto console
print_help()
{
    echo
    echo "Syntax:   smoketest.sh <test-configuration> [options]" 
    echo
    echo "Test configurations:"
    echo "    all.yaml"
    echo "        run all tests below"
    echo "    reports-test.yaml"
    echo "        testing the reports module"
    echo "    aql-test.yaml"
    echo "        testing the AQL rules module"
    echo "    lara-test.yaml"
    echo "        testing the Lara rules module"
    echo "    newsticker-test.yaml"
    echo "        testing the Newsticker module"
    echo "    ntv-test.yaml"
    echo "        testing the Network traffic vizualization module"
    echo "    syslog-test.yaml"
    echo "        testing the Syslog module"
    echo "    kite-test.yaml"
    echo "        testing the Kite module - Host reports, Host details reports, CMDB reports"
    echo 
    echo "For [options] see: https://github.com/svanoort/pyresttest"
    echo "    --log info"
    echo
}
#
if [ $# -lt 1 ]; then
    print_help
    exit 1
fi
#
IMAGE="kyn/mysqlpatchtest"
#
CONTAINER_NAME="kyn_mysqlpatchtest_1"
#
# kyn docker network autodetection
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
# Execute import_database.sql scripts for all mysql databases
#
echo "Executing import database scripts..."
RETRY=10
LOG= $( add_database )
while [ $? -ne 0 ]; do
    echo "  Mysql server not up yet, will retry in $RETRY seconds"
    sleep $RETRY
    LOG= $( add_database )
done
log_success $? "$LOG"
#
# Check differences between two databases - patched database and standalone installed here
#
echo "Checking differences between databases..."
MYOUTPUT=$(docker exec -t $CONTAINER_NAME sh -c "mysqldiff --skip-table-options --force --server1=root:kyn_RO0T_password@mysql --server2=root@localhost aql_db:aql_db")
echo $MYOUTPUT
#
#
#
echo -n "Stopping and removing container..."
LOG=$( docker stop $CONTAINER_NAME;docker rm $CONTAINER_NAME > /dev/null)
log_success $? "$LOG"
