package com.example.summarizer;

import com.example.summarizer.model.DeviceMetric;
import com.example.summarizer.model.DeviceMetricCalculator;
import com.example.summarizer.serialization.json.JsonSerdes;
import java.time.Duration;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Suppressed;
import org.apache.kafka.streams.kstream.Suppressed.BufferConfig;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DeviceMetricSummaryTopology {
  private static final Logger log = LoggerFactory.getLogger(DeviceMetricSummaryTopology.class);

  public static Topology build() {
    StreamsBuilder builder = new StreamsBuilder();
    Consumed<String, String> consumerOption = Consumed.with(Serdes.String(), Serdes.String());
    KStream<String, String> events = builder.stream("handson0429", consumerOption);

    long windowSize = 60000;
    long advancedSize = 30000;
    TimeWindows window =
        TimeWindows.ofSizeWithNoGrace(Duration.ofMillis(windowSize))
            .advanceBy(Duration.ofMillis(advancedSize));

    events
        .filter((key, value) -> value != null)
        .filter((key, value) -> !value.equals(""))
        .mapValues(value -> DeviceMetric.parse(value))
        .filter((key, value) -> !value.getDeviceId().equals(""))
        .map((key, value) -> KeyValue.pair(value.getDeviceId(), value))
        .groupByKey(Grouped.with(Serdes.String(), JsonSerdes.DeviceMetric()))
        .windowedBy(window)
        .aggregate(
            () -> new DeviceMetricCalculator(),
            (key, value, summary) -> {
              summary.add(value.getMoist(), value.getLight());
              return summary;
            },
            Materialized.as("metric-summaries")
                .with(Serdes.String(), JsonSerdes.DeviceMetricCalculator()))
        .suppress(Suppressed.untilWindowCloses(BufferConfig.unbounded()))
        .toStream()
        .map(
            (key, value) ->
                KeyValue.pair(
                    key.key(),
                    new DeviceMetric(key.key(), value.calcMoist(), value.calcLight()).stringify()))
        .to("handson0429-summary", Produced.with(Serdes.String(), Serdes.String()));

    return builder.build();
  }
}
