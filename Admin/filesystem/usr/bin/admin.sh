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
ELASTICHOST="elasticsearch"

MYSQLHOST="mysql"

DOCKER_SOCKET="/var/run/docker.sock"

# init elastic license in service container "elastisearch"
install_eslicense()
{
    echo
    /usr/bin/install_es_license.sh -h $ELASTICHOST /tmp/es_license.json
}

import_es_templates()
{
    echo
    cd /elasticsearch_data/
    ./import_template.sh -h $ELASTICHOST template/*
    cd /
}


import_es_indices()
{
    echo
    cd /elasticsearch_data/
    ./import_data.sh -h $ELASTICHOST index/*
    cd /
}


import_es_demodata()
{
    echo
    ./elasticsearch_data/import_data.sh -h $ELASTICHOST /tmp/esdemodata/*
}


check_docker_socket()
{
    [ ! -S "$DOCKER_SOCKET" ] && echo "ERROR: Docker socket at ${DOCKER_SOCKET} not mounted." && exit 1
}


dump_docker_status()
{
    echo
    echo "=== e2nw docker containers ==="
    docker ps -a --filter name=e2nw*
    echo
    echo "=== e2nw docker network ======"
    docker network list --filter name=e2nw*
    echo
    echo "============================="
    echo
}


start_e2nw()
{
    check_docker_socket
    cd /opt/e2nw
    docker-compose up -d
    cd /
    dump_docker_status
}


stop_e2nw()
{
    check_docker_socket
    cd /opt/e2nw
    docker-compose down
    cd /
    dump_docker_status
}


# patch mysql databases
patch_mysql_database()
{
    echo
    /mysql_data/patch.sh -h $MYSQLHOST -b ${1-""} -d "aql_db" -f "/mysql_data/aql/"
    echo
    /mysql_data/patch.sh -h $MYSQLHOST -b ${1-""} -d "user_management_db" -f "/mysql_data/user_management/"
}

# initializing mysql databases - creating databases and setting user access
init_mysql_db() {
    init_userdb $*
    init_aqldb $*
    set_mysql_access $*
}
# importing demo data to mysql databases
import_mysql_data() {
    echo
    /mysql_data/import_data.sh -h $MYSQLHOST -b ${1-""} /tmp/mysqldata/*
}
# init e2nw user database in service container "mysql"
init_userdb() {
    echo
    /mysql_data/user_management/init_db.sh -h $MYSQLHOST -b ${1-""}
}
# init e2nw aql database in service container "mysql"
init_aqldb() {
    echo
    /mysql_data/aql/init_db.sh -h $MYSQLHOST -b ${1-""}
}
# set mysql access rights
set_mysql_access() {
    echo
    /mysql_data/set_access.sh -h $MYSQLHOST -b ${1-""}
}

# print command line option onto console
print_help()
{
    echo
    echo "Syntax:   admin.sh [-p some_password] <command> [<command> ...]"
    echo
    echo "Commands:"
    echo "    eslicense"
    echo "        install elastic search license"
    echo "    importesdemodata"
    echo "        import elasticsearch demo data"
    echo "    up"
    echo "        start all e2nw containers"
    echo "    down"
    echo "        stop and remove all e2nw containers"
    echo "    init"
    echo "        perform all init actions at once"
    echo "    status"
    echo "        overview about running containers and networks"
    echo "    patchmysqldatabase"
    echo "        patch all mysql databases"
    echo "    initmysqldb"
    echo "        reset all mysql databases and set user access to mysql databases"
    echo "    importmysqldata"
    echo "        import data for all mysql databases"
    echo
}



##################################################
# main script
#

if [ $# -lt 1 ]; then
    print_help
    exit 1
fi

PASSWORD=""
while getopts p: OPT; do
	[ $OPT == "p" ] && PASSWORD=$OPTARG
done
#
shift $((OPTIND-1))


while [ $# -ge 1 ]
do
    case "$1" in

        eslicense)
            install_eslicense
            shift
            ;;
            
        importestemplates)
            import_es_templates
            ;;

        importesindices)
            import_es_indices
            ;;

        importesdemodata)
            import_es_demodata
            shift
            ;;
            
        up)
            start_e2nw
            ;;

        down)
            stop_e2nw
            ;;

        init)
            install_eslicense
            import_es_templates
            import_es_indices
            init_mysql_db $PASSWORD
            ;;

        status)
            dump_docker_status
            ;;

        patchmysqldatabase)
            patch_mysql_database $PASSWORD
            ;;
            
        initmysqldb)
            init_mysql_db $PASSWORD
            ;;
        
        importmysqldata)
            import_mysql_data $PASSWORD
            shift
            ;;
            
        *)
            echo "ERROR: command $1 is unknown"
            exit 1
            ;;
    esac

    shift
done
