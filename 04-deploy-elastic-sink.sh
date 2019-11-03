#!/bin/bash

CONFIG=${HOME}/Documents/Perso/Dev/talk-intro-apache-pulsar/elasticsearch-sink.yml

pulsar-admin sink create \
      --tenant talk \
      --namespace demo \
      --name elasticsearch-sink \
      --sink-type elasticsearch \
      --sink-config-file $CONFIG \
      --inputs talk/demo/orders-us

sleep 2

# check connectors
curl -s http://localhost:8080/admin/v2/functions/connectors | jq

