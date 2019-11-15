#!/bin/bash

docker start pulsar_standalone
#docker exec -it pulsar_standalone bin/pulsar standalone 

docker logs -f pulsar_standalone

