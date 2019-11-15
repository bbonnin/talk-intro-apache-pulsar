#!/bin/bash

CONFIG=${HOME}/devoxxma/elasticsearch-sink.yml


pulsar-admin sink delete  --name elasticsearch-sink --tenant demo --namespace ecommerce

sleep 2 

pulsar-admin sink create \
      --tenant demo \
      --namespace ecommerce \
      --name elasticsearch-sink \
      --sink-type elastic_search \
      --sink-config-file $CONFIG \
      --inputs demo/ecommerce/orders-us




sleep 2

# check connectors
curl -s http://localhost:8080/admin/v2/functions/connectors | jq

