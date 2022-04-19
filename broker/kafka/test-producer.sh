#!/bin/bash

./bin/kafka-console-producer.sh \
  --topic test \
  --property key.separator=, \
  --property parse.key=true \
  --bootstrap-server localhost:9092
