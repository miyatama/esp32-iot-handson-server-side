./bin/kafka-topics.sh \
  --create \
  --bootstrap-server localhost:9092 \
  --topic test  \
  --partitions 3 \
  --replication-factor 3
  
./bin/kafka-topics.sh \
  --create \
  --bootstrap-server localhost:9092 \
  --topic handson0429  \
  --partitions 3 \
  --replication-factor 3

./bin/kafka-topics.sh \
  --create \
  --bootstrap-server localhost:9092 \
  --topic handson0429-summary  \
  --partitions 3 \
  --replication-factor 3
