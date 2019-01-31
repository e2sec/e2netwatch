#!/bin/bash

URL="http://localhost:9200"


for map_file in $(ls mappings)
do
    echo setting mappings from $map_file

    INDEX=$(basename $map_file .js)

    echo "index: $INDEX"

    curl -s -XDELETE "${URL}/${INDEX}" | jq .

    curl -s -XPUT --data @mappings/${map_file}  \
        -H 'Content-Type: application/json'  \
        "${URL}/${INDEX}" | jq .
done
