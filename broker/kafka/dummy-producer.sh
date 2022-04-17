#!/bin/bash

base_moist_metric=1000
base_light_metric=0

# direction: 0 - add, 1 - sub
moist_direction=1
light_direction=0

function calculate_direction() {
  base_metric=${1}
  direction=${2}
  if [ ${base_metric} -le 0 ] ; then
    echo 0
  fi
  if [ ${base_metric} -ge 1000 ] ; then
    echo 1
  fi
  echo ${direction}
}

function calculate_base_metric() {
  base_metric=${1}
  direction=${2}
  if [ ${direction} -eq 0 ] ; then
    echo $(( ${base_metric} + 100))  
  else
    echo $(( ${base_metric} - 100))  
  fi
}

while [ 0 ] ; do
  noize=`echo $((${RANDOM} % 20))`
  moist_metric=`echo $(( ${base_moist_metric} + ${noize}))`
  noize=`echo $((${RANDOM} % 20))`
  light_metric=`echo $(( ${base_light_metric} + ${noize}))`
  message='{"deviceId":"1001","moist":'${moist_metric}',"light":'${light_metric}'}'

  ./bin/kafka-console-producer.sh \
    --topic handson0429 \
    --bootstrap-server localhost:9092 << EOF
${message}
EOF

  echo ${message}

  # recalc base moist
  base_moist_metric=`calculate_base_metric ${base_moist_metric} ${moist_direction}`
  moist_direction=`calculate_direction ${base_moist_metric} ${moist_direction}`

  base_light_metric=`calculate_base_metric ${base_light_metric} ${light_direction}`
  light_direction=`calculate_direction ${base_light_metric} ${light_direction}`
  sleep 1
done
