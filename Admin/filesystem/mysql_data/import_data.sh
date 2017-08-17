#!/bin/sh

HOST="localhost"
PORT="3306"
PASSWORD=""
#
while getopts h:p:b:l: OPT; do
	[ $OPT == "h" ] && HOST=$OPTARG
	[ $OPT == "p" ] && PORT=$OPTARG
    [ $OPT == "b" ] && PASSWORD=$OPTARG
done
#
shift $((OPTIND-1))

# Import demo data from all scripts in directory
for DATA_FILE in $*
do	
	echo -n "Importing demo data from $DATA_FILE..."
    mysql -h $HOST -P $PORT -w --password=$PASSWORD < $DATA_FILE
    echo "done."
done
