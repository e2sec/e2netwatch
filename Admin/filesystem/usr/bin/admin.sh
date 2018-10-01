#!/bin/sh
#
# Name: init.sh
#
# Author: Tobias Nieberg
#
# Purpose: Initialise e2nw system
#
# Usage: init.sh <actions>
#
################################################################

# initializing mysql databases - creating databases and setting user access
init_mysql_db() {
    echo
    /mysql_data/init_database.sh
}

# print command line option onto console
print_help()
{
    echo
    echo "Syntax:   admin.sh <command> [<command> ...]"
    echo
    echo "Commands:"
    echo "    initmysqldb"
    echo "        reset all mysql databases and set user access to mysql databases"
    echo
}



##################################################
# main script
#

if [ $# -lt 1 ]; then
    print_help
    exit 1
fi


while [ $# -ge 1 ]
do
    case "$1" in

        initmysqldb)
            init_mysql_db
            ;;

        *)
            echo "ERROR: command $1 is unknown"
            exit 1
            ;;
    esac

    shift
done
