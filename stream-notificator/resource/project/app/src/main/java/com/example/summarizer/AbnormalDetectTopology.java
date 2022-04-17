package com.example.summarizer;

import com.example.summarizer.model.DeviceMetric;
import com.example.summarizer.model.DeviceMetricAbnormalDetector;
import com.example.summarizer.serialization.json.JsonSerdes;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class AbnormalDetectTopology {
  private static final Logger log = LoggerFactory.getLogger(AbnormalDetectTopology.class);

  public static Topology build() {
    StreamsBuilder builder = new StreamsBuilder();
    Consumed<String, String> consumerOption = Consumed.with(Serdes.String(), Serdes.String());
    KStream<String, String> events = builder.stream("handson0429-summary", consumerOption);

    events
        .filter((key, value) -> value != null)
        .filter((key, value) -> !value.equals(""))
        .mapValues(value -> DeviceMetric.parse(value))
        .filter((key, value) -> !value.getDeviceId().equals(""))
        .map((key, value) -> KeyValue.pair(value.getDeviceId(), value))
        .groupByKey(Grouped.with(Serdes.String(), JsonSerdes.DeviceMetric()))
        .aggregate(
            () -> new DeviceMetricAbnormalDetector(),
            (key, value, detector) -> {
              detector.detect(value.getMoist(), value.getLight());
              return detector;
            },
            Materialized.as("metric-abnormal-detector")
                .with(Serdes.String(), JsonSerdes.DeviceMetricAbnormalDetector()))
        .filter((key, value) -> value.abnormal)
        .toStream()
        .foreach(
            (key, value) -> {
              try {
                var client = HttpClient.newHttpClient();
                var json = "{ \"message\" : \"" + key + "が眩しいってさ\" }";
                var req =
                    HttpRequest.newBuilder()
                        .uri( URI.create( System.getenv("NOTIFICATE_ENDPOINT")))
                        .header("Content-Type", "application/json")
                        .POST(BodyPublishers.ofString(json))
                        .build();
                var res = client.send(req, HttpResponse.BodyHandlers.ofString());
                System.out.println(res.body());
              } catch (Exception e) {
                System.out.println(e.toString());
              }
            });

    return builder.build();
  }
}
