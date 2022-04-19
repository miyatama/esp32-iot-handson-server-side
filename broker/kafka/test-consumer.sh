#!/bin/bash

./bin/kafka-console-consumer.sh \
  --topic test \
  --from-beginning \
  --property print.timestamp=true \
  --property print.key=true \
  --property print.value=true \
  --bootstrap-server localhost:9092
