#
# Name: Dockerfile
#
# Author: Istok Pavlovic
#
# Date: 2-08-2018
#
# Docker file to build the middleware.
#
# This multistage dockerfile first builds an app and then 
# copyes that app into new container that runs the app.
# Only when code chages will first part of the build be executed.
# 
#######################################################################

FROM java:openjdk-8-jdk as build

ENV MAVEN_VERSION 3.3.9

ARG VARIANT

RUN mkdir -p /usr/share/maven \
  && curl -fsSL http://apache.osuosl.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz \
    | tar -xzC /usr/share/maven --strip-components=1 \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven

ADD ./e2nwKite /tmp/middleware/e2nwKite

WORKDIR /tmp/middleware/e2nwKite/e2nwKiteWebApp

RUN /usr/bin/mvn -P$VARIANT package 


FROM jetty:alpine
USER root
RUN apk update \
    && apk add --no-cache socat supervisor \
    && mkdir -p /var/log/supervisor

COPY --from=build /tmp/middleware/e2nwKite/e2nwKiteWebApp/target/e2nwKiteWebApp.war /var/lib/jetty/webapps/.

ADD ./filesystem /

EXPOSE 8080

ENTRYPOINT ["/usr/bin/supervisord", "-c", "/etc/supervisord.conf"]
