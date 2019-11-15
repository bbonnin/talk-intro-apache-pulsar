#!/bin/bash

export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_211.jdk/Contents/Home
export PATH=$JAVA_HOME/bin:$PATH

pulsar sql-worker run 


