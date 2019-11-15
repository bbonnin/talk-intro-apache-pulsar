#!/bin/bash

pulsar-client consume \
    -n 0 -r 1 \
    -s "demo-ecommerce-orders-us-exclusive" \
    -t Exclusive \
    demo/ecommerce/orders-us 

