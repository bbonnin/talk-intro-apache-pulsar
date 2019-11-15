#!/bin/bash

JAR=$(pwd)/Documents/Perso/Dev/talk-intro-apache-pulsar/examples/target/pulsar-examples-jar-with-dependencies.jar

pulsar-admin functions localrun \
   --jar $JAR \
   --className io.millesabords.pulsar.examples.ecommerce.FilterByCountryFunction \
   --fqfn talk/demo/orderfilter \
   --inputs persistent://talk/demo/orders-all \
   --output persistent://talk/demo/orders-but-us \
   --user-config '{dbpath: "'$(pwd)'/Documents/Perso/Dev/talk-intro-apache-pulsar/examples/GeoLite2-Country.mmdb"}' \
   --state-storage-service-url bk://localhost:4181

