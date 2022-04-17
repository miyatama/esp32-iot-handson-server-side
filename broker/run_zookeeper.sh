#!/bin/bash
cd `dirname $0`
cd ./kafka

./bin/zookeeper-server-start.sh ./config/zookeeper.properties &
wait
