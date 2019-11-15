#!/bin/bash

pulsar-admin schemas get demo/ecommerce/orders-all | jq -r '.schema' | jq

#pulsar-admin schemas get demo/ecommerce/orders-all | jq -r '.schema' | base64 -D | jq

