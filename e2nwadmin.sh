#!/bin/sh

# Copyright (C) 2017 e-ito Technology Services GmbH
# e-mail: info@e-ito.de

# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.

# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.

# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.

### grep all environment variables with prefix e2nw_*
#
get_environment()
{
    env | grep e2nw_ | while read line
    do
        [ -n "${line}" ] && echo "--env $line "
    done
}

### e2nw docker network autodetection
#
get_network()
{
    e2nw_NETWORK=$(docker network list --filter name=e2nw* --format {{.Name}})

    if [ ! -z "$e2nw_NETWORK" ]; then
        NUM_NETWORKS=$(echo $e2nw_NETWORK | wc -w)
        if [ $NUM_NETWORKS -gt 1 ]; then
            echo "WARNING: more than 1 e2nw* network found! [" $e2nw_NETWORK "]" 1>&2
            e2nw_NETWORK=$(echo $e2nw_NETWORK|cut -f1 -d" ")
            echo "using $e2nw_NETWORK" 1>&2
        fi
        echo "--network=$e2nw_NETWORK"
    fi
}

### setting sqldata volume if needed
#
set_sqldata_volume()
{
    # convert arguments to array and loop through to find command name
    array=( "$@" )
    for i in ${!array[@]};
    do
        if [ ${array[$i]} == "importmysqldata" ]; then
            # check if next argument is valid directory and if yes set to mount point
            if [ -d "${array[$((i+1))]}" ]; then
                MYSQL_DATA_DIRECTORY_PATH=`realpath ${array[$((i+1))]}`
                VOLUME="$VOLUME --volume=$MYSQL_DATA_DIRECTORY_PATH:/tmp/mysqldata"
            else
                echo "Importing data into mysql database requests directory path containing .sql files as second argument!"
                exit 1
            fi
            break
        fi
    done
}

#######################################################################
#
### e2nw docker parameters
#
VOLUME="--volume=/var/run/docker.sock:/var/run/docker.sock"
#
IMAGE="e2nw/admin"
#
ENV=$(get_environment)
#
# Special case for "down" command
if [ "$1" != "down" ]; then
    NETWORK=$(get_network)
fi

# Special case for "eslicense" command
if [ "$1" = "eslicense" ]; then
    if [ -f "$2" ]; then
        ES_FILE_PATH=`realpath $2`
        VOLUME="$VOLUME --volume=$ES_FILE_PATH:/tmp/es_license.json"
    else
        echo "Installing an ES license requires a license file as second argument!"
        exit 1
    fi
# Special case for "importesdemodata" command
elif [ "$1" = "importesdemodata" ]; then
    if [ -d "$2" ]; then
        ES_DATA_DIRECTORY_PATH=`realpath $2`
        VOLUME="$VOLUME --volume=$ES_DATA_DIRECTORY_PATH:/tmp/esdemodata"
    else
        echo "Importing demo data into Elasticsearch requests directory path containing .json files as second argument!"
        exit 1
    fi
fi

# Special case for "importmysqldata" command
set_sqldata_volume $@

#######################################################################
#
### run command in docker admin container
docker run --rm ${ENV} ${VOLUME} ${NETWORK} ${IMAGE} $@
