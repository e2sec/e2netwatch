### The build image ###
#
#   Image contains a java development kit and maven build system.
#   Application is build during image build.
#
FROM openjdk:8 AS build

RUN apt-get update -y && apt-get install -y maven

RUN mkdir -p /nfa/src 

WORKDIR /nfa

# add sources and maven build instructions
ADD pom.xml .
ADD src     ./src

# build application
RUN mvn verify


### The runtime image ###
#
#   Image contains a java runtime environment and the java application
#   (copied from build container)
FROM openjdk:8-jre-alpine

COPY --from=build /nfa/target/*.jar /
