#!/bin/bash

for query_data in $(ls queries/* )
do
    echo adding query from $query_data
    #cat $query_data

    curl --header "Content-Type: application/json" --data @${query_data}  http://localhost:26578/api/register
    echo
done
