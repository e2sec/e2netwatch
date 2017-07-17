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
# Usage: mysqlpatchtest.sh -d "database1 [database2 ...]" [-p mysqlrootpassword] [-q]
#
####################################################################################################
#
# Include convenience functions.
#
. ./ci_scripts/common
#
# Custom version of echo function which first checks if logging is enabled
#
my_echo() {
    [ $LOGGING -eq 0 ] && return
    local OPTIND
    local same_line=0
    local message=""
    while getopts nm: OPT; do
        [ $OPT == "n" ] && same_line=1
        [ $OPT == "m" ] && message=$OPTARG
    done
    shift $((OPTIND-1))
    #
    if [ $same_line -eq 1 ]; then
        echo -n "$message"
    else
        echo "$message"
    fi
}
#
# Get check answer from mysql server
#
mysql_check() {
    docker exec -t kyn_mysqlpatchtest_1 mysql -e "select 1"
}
#
# Wait for Mysql to start
#
# SAFETY_CHECK_* - For some reason mysql server goes up, and after couple of seconds down again, and then up again. To verify if it is up definitely, we use "safety check"
#
wait_mysql_start() {
    my_echo -n -m "Waiting for mysql to start"
    local RETRY=3
    local SAFETY_CHECK_MAX=5
    local safety_check_current=$SAFETY_CHECK_MAX
    local log=$( mysql_check > /dev/null )
    local exit_code=$?
    while [ $exit_code -ne 0 ] || [ $safety_check_current -gt 0 ]; do
        if [ $exit_code -eq 0 ]; then
            safety_check_current=$((safety_check_current-1))
        else
            safety_check_current=$SAFETY_CHECK_MAX
        fi
        my_echo -n -m "."
        sleep $RETRY
        log=$( mysql_check > /dev/null )
        exit_code=$?
    done
    my_echo -m "$( log_success $exit_code "$log" )"
}
#
# Run sql scripts to import databases
#
add_databases() {
    local database
    local log
    for database in $DATABASES
    do
        my_echo -n -m "Importing test database $database..."
        local log=$( docker exec -t $CONTAINER_NAME sh -c "mysql < ./tmp/scripts/'$database'.sql" > /dev/null )
        my_echo -m "$( log_success $? "$log" )"
    done
}
#
# Check differences between two databases - patched database and standalone installed here
#
check_differences() {
    my_echo -m "Checking differences for databases..."
    local grep_expression="^# WARNING: Objects in server|^# Comparing .*\[FAIL\]"
    local database
    local log
    local exit_code
    local differences
    for database in $DATABASES
    do
        my_echo -m "Checking difference for $database..."
        log=$( docker exec -t $CONTAINER_NAME sh -c "mysqldiff --skip-table-options --force --server1=root:'$PASSWORD'@mysql --server2=root@localhost '$database':'$database'" )
        exit_code=$?
        # Check if command executed properly
        if [ $exit_code -ne 0 ]; then
            my_echo -m "$log"
            remove_container
            exit $exit_code
        fi
        differences=$(echo "$log" | grep -E "$grep_expression")
        differences_number=$(echo "$log" | grep -E "$grep_expression" -c)
        if [ $differences_number -eq 0 ]; then
            echo "SUCCESS [$database]"
        else
            echo "FAIL [$database]"
            my_echo -m "$differences"
        fi
    done
}
#
# Stop and remove running container
#
remove_container() {
    my_echo -n -m "Stopping and removing container..."
    local log=$( docker stop $CONTAINER_NAME;docker rm -v $CONTAINER_NAME > /dev/null)
    my_echo -m "$( log_success $? "$log" )"
}
#
# Create and start container for testing mysql patch
#
start_container() {
    my_echo -n -m "Starting mysql patch test container..."
    local log=$( docker run -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -d --name=$CONTAINER_NAME $NETWORK $IMAGE > /dev/null )
    my_echo -m "$( log_success $? "$log" )"
}
#
# print command line option onto console
#
print_help()
{
    echo
    echo "Syntax:   mysqlpatchtest.sh -d \"database_name_1 [database_name_2 ...]\" [-p some_password] [-q]"
    echo
    echo "Options:"
    echo "    -d"
    echo "        list databases to test"
    echo "    -p"
    echo "        Mysql root password"
    echo "    -q"
    echo "        run \"quietly\" with just final info about success"
    echo
}
#
# Getting the function arguments
#
PASSWORD=""
LOGGING=1
DATABASES=
while getopts p:qd: OPT; do
	[ $OPT == "p" ] && PASSWORD=$OPTARG
    [ $OPT == "q" ] && LOGGING=0
    [ $OPT == "d" ] && DATABASES=$OPTARG
done
shift $((OPTIND-1))
if [ -z "$DATABASES" ]; then
    [ $LOGGING -eq 1 ] && print_help
    exit
fi
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
start_container
#
# Wait Mysql to start
#
wait_mysql_start
#
# Import test databases
#
add_databases
#
# Check differences for all databases
#
check_differences
#
# Stop and remove container
#
remove_container
