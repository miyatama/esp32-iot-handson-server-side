package com.example.summarizer;

import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;

class App {
  public static void main(String[] args) {
    Topology topology = AbnormalDetectTopology.build();

    Properties props = new Properties();
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "device-metric-summarizer");
    props.put(
        StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
        "kafka-broker-001:9092,kafka-broker-002:9092,kafka-broker-003:9092");
    props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
    props.put(StreamsConfig.STATE_DIR_CONFIG, "state-store");
    props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 10 * 1000);
    props.put(
        StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class);

    System.out.println(topology.describe());
    KafkaStreams streams = new KafkaStreams(topology, props);
    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    streams.cleanUp();
    streams.start();
  }
}
