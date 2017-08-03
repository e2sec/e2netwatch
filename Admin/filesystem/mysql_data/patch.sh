#!/bin/sh
#
# Patch database script
#
# Author: Hrvoje Zeljko
#
# Date: 03-07-2017
#
# Purpose: Patch aql_db database
#
# Usage: patche.sh [-h host] [-p port] [-b password] [-d database] [-f pathtoversion]
#
####################################################################################################
#
# Check version numbers
#
# Params:   $1 - version number in database
#           $2 - version number in repository file
#
check_version_numbers()
{
    # Check irregular version condition
    if [ $1 -gt $2 ]; then
        echo -e "\e[31mIrregular condition - database version number should never be greater than repository version number\e[0m"
        exit 1
    fi
    # Check if no patch necessary
    if [ $1 -eq $2 ]; then
        echo -e "\e[33mNo patch necessary\e[0m"
        exit 1
    fi
}
#
# Getting input parameters
#
HOST="localhost"
PORT="3306"
PASSWORD=""
DATABASE=""
VERSION_FILENAME_PATH=""
#
while getopts h:p:b:d:f: OPT; do
	[ $OPT == "h" ] && HOST=$OPTARG
	[ $OPT == "p" ] && PORT=$OPTARG
    [ $OPT == "b" ] && PASSWORD=$OPTARG
    [ $OPT == "d" ] && DATABASE=$OPTARG
    [ $OPT == "f" ] && VERSION_FILENAME_PATH=$OPTARG
done
#
shift $((OPTIND-1))
#
# Starting message
#
echo "Patching $DATABASE"
#
# Getting the database version number
#
echo -n "Getting database version number..."
VERSION_NUMBER_DB=$(echo "select version_number from version" | mysql -h $HOST -P $PORT -w --password=$PASSWORD --database=$DATABASE -s)
echo "Database version number is $VERSION_NUMBER_DB."
#
# Getting the repository version number
#
VERSION_FILENAME=${VERSION_FILENAME_PATH}"version"
echo -n "Getting repository version number..."
VERSION_NUMBER_REPOSITORY="$(cat $VERSION_FILENAME)"
echo "Repository version number is $VERSION_NUMBER_REPOSITORY."
#
# Check version numbers
#
check_version_numbers $VERSION_NUMBER_DB $VERSION_NUMBER_REPOSITORY
#
# Execute all necessary patches
#
echo "Executing patches..."
ALL_PATCHES_OK=true
for version in $(seq $((VERSION_NUMBER_DB+1)) $VERSION_NUMBER_REPOSITORY)
do 
    echo -n "Executing patch_$version.sql..."
    mysql -h $HOST -P $PORT -w --password=$PASSWORD --database=$DATABASE < ${VERSION_FILENAME_PATH}sql_scripts/patch/patch_${version}.sql
    if [ $? -ne 0 ]; then
        ALL_PATCHES_OK=false
    fi
    echo "done"
done
# Check if all patches successfully executed
if $ALL_PATCHES_OK ; then
    echo -e "\e[32mAll patches executed correctly\e[0m"
    #
    # Update database version number
    #
    echo -n "Updating database version number from $VERSION_NUMBER_DB to $VERSION_NUMBER_REPOSITORY..."
    mysql -h $HOST -P $PORT -w --password=$PASSWORD --database=$DATABASE -e "update version set version_number=$VERSION_NUMBER_REPOSITORY"
    echo "done"
else
    echo -e "\e[31mNot all patches executed correctly\e[0m"
fi
