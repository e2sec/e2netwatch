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

RUN mkdir -p /usr/share/maven \
  && curl -fsSL http://apache.osuosl.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz \
    | tar -xzC /usr/share/maven --strip-components=1 \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven

ADD ./src /tmp/middleware/src

WORKDIR /tmp/middleware/src/RestAPI

RUN /usr/bin/mvn package 


FROM jetty:alpine

COPY --from=build /tmp/middleware/src/RestAPI/target/middleware.war /var/lib/jetty/webapps/.
