#!/bin/bash

java -cp Documents/Perso/Dev/talk-intro-apache-pulsar/examples/target/pulsar-examples-jar-with-dependencies.jar \
  -Dorg.apache.logging.log4j.simplelog.StatusLogger.level=OFF \
  io.millesabords.pulsar.examples.ecommerce.OrderGenerator
 
