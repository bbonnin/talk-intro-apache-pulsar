#!/bin/bash

pulsar-client produce \
    -m "hello SunnyTech" \
    -n 100 -r 1 \
    demo-topic

