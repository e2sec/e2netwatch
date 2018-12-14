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
ADD ./src /tmp/middleware/src
WORKDIR /tmp/middleware/src
RUN ./mvnw clean install

FROM openjdk:8-jdk-alpine
COPY --from=build /tmp/middleware/src/application/target/middleware.war .
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/middleware.war"]
