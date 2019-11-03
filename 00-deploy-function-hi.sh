#!/bin/bash

JAR=${HOME}/Documents/Perso/Dev/talk-intro-apache-pulsar/examples/target/pulsar-examples-jar-with-dependencies.jar

#JAR=/pulsar/fct/pulsar-examples-jar-with-dependencies.jar

pulsar-admin functions create \
   --jar $JAR \
   --className io.millesabords.pulsar.examples.EnhancedHiFunction \
   --fqfn talk/demo/hello \
   --inputs persistent://talk/demo/input \
   --output persistent://talk/demo/output \
   --log-topic persistent://talk/demo/logs


echo 
echo Liste des fonctions:

pulsar-admin functions list --namespace demo --tenant talk
