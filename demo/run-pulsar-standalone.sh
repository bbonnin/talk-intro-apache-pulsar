#!/bin/bash

docker run -it \
  -h pulsar \
  --name pulsar_standalone \
  -p 6650:6650 \
  -p 8080:8080 \
  -p 4181:4181 \
  -p 2181:2181 \
  -v $PWD/data:/pulsar/data \
  apachepulsar/pulsar:2.4.1 \
  bin/pulsar standalone

