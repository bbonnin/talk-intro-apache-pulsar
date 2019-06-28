#!/bin/bash

pulsar-client consume \
    -n 0 -r 1 \
    -s "demo-subs-exclusive" \
    -t Exclusive \
    demo-topic

