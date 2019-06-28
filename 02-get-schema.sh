#!/bin/bash

pulsar-admin schemas get talk/demo/orders-all

pulsar-admin schemas get talk/demo/orders-all | jq -r '.schema' | base64 -D | jq

