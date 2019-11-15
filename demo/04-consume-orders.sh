#!/bin/bash

pulsar-client consume -n 0 \
  -s "demo-orders-all-exclusive" \
  -t Exclusive \
  demo/ecommerce/orders-all
