#!/bin/bash

pulsar-client consume \
    -n 0 -r 1 \
    -s "demo-ecommerce-orders-logs-exclusive" \
    -t Exclusive \
    demo/ecommerce/orders-logs

