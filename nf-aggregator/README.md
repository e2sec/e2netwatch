# Netflow flow aggregation
This is the Java source code repository for netflow flow aggregation module.
## Description
Netflow data always shows only one direction of ip communication.

This piece of software identifies correlating flows and generates connection information.
## Table of Contents
* Build
* Hints

### Build
This is a Maven project with automatic resolving dependencies.

To compile everything into one jar file for roleout use
```
mvn verify
```
The jar file will be placed in the target subfolder.

To compile and run the the application locally use
```
mvn compile exec:java [-Dexec.args="<command line parameter>"]
```

To run application from jar use
```
java -jar ./target/netflow-flowaggregation-1.0-SNAPSHOT-jar-with-dependencies.jar [<command line parameter>]
```

Available commandline parameters:
```
-c <path to config file>
```

### Hints
If there's no given config file, default is:

  * Kafka needs to run at localhost:9092
  * Kafka topic for incoming netflow data is nfin
  * Kafka topic for outgoing connection data is nfout

