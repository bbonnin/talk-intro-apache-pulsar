#!/bin/bash

export PULSAR_LOG_CONF=${HOME}/devoxxma/demo/log4j2.xml
#JAR=${HOME}/devoxxma/examples/target/pulsar-examples-jar-with-dependencies.jar
JAR=${HOME}/devoxxma/examples/target/pulsar-examples-1.0-SNAPSHOT.nar
DB_PATH=${HOME}/devoxxma/examples/GeoLite2-Country.mmdb

pulsar-admin functions localrun \
   --jar ${JAR} \
   --className io.millesabords.pulsar.examples.ecommerce.FilterByCountryFunction \
   --fqfn demo/ecommerce/orderfilterbycountry \
   --inputs persistent://demo/ecommerce/orders-all \
   --output persistent://demo/ecommerce/orders-but-us \
   --log-topic persistent://demo/ecommerce/orders-logs \
   --user-config '{dbpath: "'${DB_PATH}'"}'

#--state-storage-service-url bk://localhost:4181