JMSLooker
=========

This small utility fetches the messages off of a Glassfish OpenMQ
message bus and prints each message to a separate file. It was
developed as a tool for debugging JMS applications.


Building
--------

Start by installing maven 3 and then run

```
mvn package
```

Running
-------

```
java -jar target/jsmlooker-1.0-SNAPSHOT-fat.jar
```