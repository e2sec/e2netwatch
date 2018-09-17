e2netwatch
=================

This is the repository for the e2netwatch project. It contains all files needed to build container images for e2netwatch. To run the system properly you may need some additional data (e.g. an elasticsearch license file) that is not included in this repository, but must be provided from somewhere else.

# Description

e2netwatch is a "SIEM toolbox". It is a system that provides several components that can be combined with huge flexibility to provide SIEM (Security Information and Event Management) and network monitoring functionality. Combining and aggregating its input sources it is able to create events, detect anomalies and trigger alerts for unusual and threatening events.
Analysis is based on a high performance stream processing engine and machine learning of wanted behaviour.


# Table of Contents

# Prerequisites

Before you can start e2netwatch, please make sure you have installed Docker and Docker Compose on your machine. Detailed information about how to install docker onto your system can be found here: https://docs.docker.com/engine/installation/, instructions about docker compose installation can be obtained here: https://docs.docker.com/compose/install/.

# Build
Building e2netwatch toolset is a one-liner. Just build the docker container images before starting them. Warning: this may take some time on the first time!

    Build all docker images of e2netwatch in a batch:
    $ docker-compose build


## Start the e2nw project

Prerequisites: On some linux systems it is needed to adjust virtual memory settings that are needed to run elasticsearch instance properly. You can adjust them by executing script increase_vm_map_count.sh:
    
    $ ./increase_vm_map_count.sh

After that start the complete e2netwatch system by executing
    
    $ docker-compose up -d      # start all containers in the background

If you are interested in watching all the logs during start time, you can start them in foreground:
    
    $ docker-compose up

Same commands can be used to restart or more containers after an image has been changed. Docker knows which images ahve been changes and will resatrt only the changed ones.
    
    $ docker-compose up

To shut down the system, shut down all containers using docker-compose:
    
    $ docker-compose down


# Initialisation

Prerequisites: On some linux systems it is needed to adjust virtual memory settings that are needed to run elasticsearch instance properly. You can adjust them by executing script increase_vm_map_count.sh:
    $ ./increase_vm_map_count.sh

After that start the complete e2netwatch system by executing
    $ docker-compose up -d      # start all containers in the background

If you are interested in watching all the logs during start time, you can start them in foreground:
    $ docker-compose up

Same commands can be used to restart or more containers after an image has been changed. Docker knows which images ahve been changes and will resatrt only the changed ones.
    $ docker-compose up

To shut down the system, shut down all containers using docker-compose:
    $ docker-compose down


# Usage

After the start of the e2netwatch containers, you are able to access the web frontend of the e2netwatch project. Go to you Browser and type in the IP address of your server. From this interface most of the functions are available.
If you want to access the kibana webinterface, go to your Browser and type in http://<e2netwatch_host>:5601.
To access the NiFi web interface, go to http://<e2netwatch_host>:8090.


# Contributing

# Credits

# License

