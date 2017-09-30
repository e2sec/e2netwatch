#
# Name: Dockerfile
#
# Date: 12-07-2016
#
# Docker file to deploy the middleware. 
#
# Use of this file to generate an image assumes that the middleware
# has already been built.
#
#######################################################################
#
FROM jetty
#
# Add packages
#
USER root
RUN apt-get update \
    && apt-get install -y socat \
                          supervisor \
                          vim
#
# Add the application
#
COPY e2nwKite/e2nwKiteWebApp/target/e2nwKiteWebApp /var/lib/jetty/webapps/e2nwKiteWebApp
#
ADD filesystem /
#
EXPOSE 8080
#
# Set the default command.
#
ENTRYPOINT ["/usr/bin/supervisord", "-c", "/etc/supervisord.conf"]
#
#######################################################################
