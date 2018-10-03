#!/bin/bash
#
# Name: init_database.sh
#
# Author: Hrvoje Zeljko
#
# Purpose: Initialize Mysql database on new hosts
#
# Usage: init_database.sh
#
################################################################
#
# Define vars
CONTAINER_NAME=mysql-init-container
SERVICE_NAME=mysql
SQL_SCRIPT_NAME=init_database.sql
#
# Start temporary Mysql container
docker-compose run --name $CONTAINER_NAME -d $SERVICE_NAME
#
# Copy sql script to running container
docker cp $SQL_SCRIPT_NAME $CONTAINER_NAME:/
#
#
echo -n "Initialize Mysql database."
output=$( docker exec $CONTAINER_NAME sh -c "mysql < /$SQL_SCRIPT_NAME" 2>&1 )
while [ "$?" -ne "0" ]
do
    # Check if database is already initialized
    echo $output | grep "Access denied for user" > /dev/null
    if [ "$?" -eq "0" ]
    then
        echo "ERROR"
        docker-compose down
        echo $output
        exit 1
    fi
    sleep 1
    echo -n "."
    output=$( docker exec $CONTAINER_NAME sh -c "mysql < /$SQL_SCRIPT_NAME" 2>&1 )
done
echo "done"
#
# Destroy temporary Mysql container
docker-compose down
