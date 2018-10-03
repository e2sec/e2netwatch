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


# print command line option onto console
print_help()
{
    echo
    echo "Syntax:   admin.sh <command> [<command> ...]"
    echo
    echo "Commands:"
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

        *)
            echo "ERROR: command $1 is unknown"
            exit 1
            ;;
    esac

    shift
done
