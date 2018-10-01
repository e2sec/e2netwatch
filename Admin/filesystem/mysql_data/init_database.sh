#!/bin/sh

HOST="localhost"
PORT="3306"
PASSWORD=""
#
while getopts h:p:b: OPT; do
	[ $OPT == "h" ] && HOST=$OPTARG
	[ $OPT == "p" ] && PORT=$OPTARG
    [ $OPT == "b" ] && PASSWORD=$OPTARG
done
#
shift $((OPTIND-1))

# Initialize Mysql database
echo -n "Initializing Mysql database..."
mysql -h $HOST -P $PORT -w --password=$PASSWORD < /mysql_data/sql_scripts/init_database.sql
echo "done."
