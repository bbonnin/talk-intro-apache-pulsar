#!/bin/bash

pulsar-client produce \
    -m "Hello Morocco !!" \
    -n 100 -r 1 \
    demo-topic

