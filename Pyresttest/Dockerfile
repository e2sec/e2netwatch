#
# Docker file to build the Pyresttest environment
#
####################################################################################################
FROM debian:jessie
#
RUN apt-get update && \
        apt-get install -y python-pycurl \
                            python-pip && 
                            pip install pyresttest && 
                            rm -rf /var/lib/apk/lists/*
#
COPY ./filesystem/tests /tests
#
WORKDIR /tests
#
ENTRYPOINT ["pyresttest", "http://e2nwmiddleware:8080"]
#
####################################################################################################
