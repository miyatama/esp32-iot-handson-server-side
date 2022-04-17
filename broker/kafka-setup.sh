i#!/bin/bash

sudo apt update -y
sudo apt upgrade -y
sudo apt install -y \
  apt-transport-https \
  ca-certificates \
  wget \
  dirmngr \
  gnupg \
  curl \
  software-properties-common \
  iputils-ping \
  net-tools \
  procps
wget -qO - https://adoptopenjdk.jfrog.io/adoptopenjdk/api/gpg/key/public | sudo apt-key add -
sudo add-apt-repository --yes https://adoptopenjdk.jfrog.io/adoptopenjdk/deb/
sudo apt update
sudo apt install -y \
  adoptopenjdk-8-hotspot
curl -OL https://dlcdn.apache.org/kafka/3.1.0/kafka_2.13-3.1.0.tgz
tar fx kafka_2.13-3.1.0.tgz
rm -fr kafka_2.13-3.1.0.tgz
mv kafka_2.13-3.1.0 kafka

# data directory settings
sudo mkdir -p /var/lib/zookeeper
sudo chown $(whoami):$(whoami) /var/lib/zookeeper
sudo mkdir -p /var/lib/kafka/data
sudo chown -R $(whoami):$(whoami) /var/lib/kafka
mkdir ./kafka/console_log

cd ./kafka/config

# setting environment
ZOOKEEPERS="server.1=kafka-broker-001:2888:3888;server.2=kafka-broker-002:2888:3888;server.3=kafka-broker-003:2888:3888"
KAFKA_BROKERS="kafka-broker-001:2181,kafka-broker-002:2181,kafka-broker-003:2181"
ZOOKEEPER_ID=`hostname | sed 's/[^-]*-[^-]*-\(.*\)/\1/g' | sed 's/^0*//g'`
KAFKA_BROKER_ID=`hostname | sed 's/[^-]*-[^-]*-\(.*\)/\1/g' | sed 's/^0*//g'`

# zookeeper settings
sed -i -e 's#dataDir=/tmp/zookeeper#dataDir=/var/lib/zookeeper#g' zookeeper.properties
echo 'initLimit=10' >> ./zookeeper.properties
echo 'syncLimit=5' >> ./zookeeper.properties
echo "${ZOOKEEPERS}" | \
    sed "s/;/\n/g" >> ./zookeeper.properties
echo ${ZOOKEEPER_ID} > /var/lib/zookeeper/myid

# kafka settings
sed -i -e 's#log.dirs=/tmp/kafka-logs#log.dirs=/var/lib/kafka/data#g' server.properties
echo 'broker.id.generation.enable=false' >> server.properties
sed -i -e 's/^zookeeper.connect=/#zookeeper.connect=/g' server.properties
sed -i -e 's/broker.id=0/# broker.id=0/g' ./server.properties
echo "broker.id=${KAFKA_BROKER_ID}" >> ./server.properties
echo "zookeeper.connect=${KAFKA_BROKERS}" >> ./server.properties
mygip=`curl inet-ip.info`
echo "advertised.listeners=PLAINTEXT://${mygip}:9092" >> ./server.properties
