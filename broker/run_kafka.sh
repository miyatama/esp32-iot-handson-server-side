#!/bin/bash
cd `dirname $0`
cd kafka

./bin/kafka-server-start.sh ./config/server.properties &
wait
