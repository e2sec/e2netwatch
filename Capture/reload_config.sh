#!/bin/sh

### force logstash to reload config file (send signal SIGHUP to process) ###

CONTAINER="e2nwcapture"

docker exec $CONTAINER kill -1 1
