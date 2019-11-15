#!/bin/bash

java -cp ${HOME}/devoxxma/examples/target/pulsar-examples.jar \
  -Dorg.apache.logging.log4j.simplelog.StatusLogger.level=OFF \
  io.millesabords.pulsar.examples.ecommerce.OrderGenerator
 
