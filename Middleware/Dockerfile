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

FROM openjdk:10.0.2 as build

ENV MAVEN_VERSION 3.3.9

RUN mkdir -p /usr/share/maven \
  && curl -fsSL http://apache.osuosl.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz \
   | tar -xzC /usr/share/maven --strip-components=1 \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven

ADD ./src /tmp/middleware/src

WORKDIR /tmp/middleware/src/RestAPI

RUN /usr/bin/mvn package 

RUN wget https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-distribution/9.4.11.v20180605/jetty-distribution-9.4.11.v20180605.tar.gz \
    && tar xzvf jetty-distribution-9.4.11.v20180605.tar.gz  \
    && rm jetty-distribution-9.4.11.v20180605.tar.gz \
    && mv jetty-distribution-9.4.11.v20180605/ /jetty/

COPY --from=build /tmp/middleware/src/RestAPI/target/middleware.war /jetty/webapps/.

WORKDIR /jetty

CMD java --add-modules java.xml.bind -jar start.jar

