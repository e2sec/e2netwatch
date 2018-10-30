#!/bin/bash

### add default targets "netflow" and "syslog" to Norikra for processing these streams ###

for target_data in $(ls targets/* )
do
    echo adding target from $target_data
    cat $target_data

    curl --header "Content-Type: application/json" --data @${target_data}  http://localhost:26578/api/open
    echo
done
