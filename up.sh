#!/bin/bash

COMPONENTS="$*"

docker-compose up -d $COMPONENTS
