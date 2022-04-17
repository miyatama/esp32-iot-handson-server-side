package com.example.summarizer.serialization.json;

import com.example.summarizer.model.DeviceMetric;
import com.example.summarizer.model.DeviceMetricCalculator;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public class JsonSerdes {
  public static Serde<DeviceMetric> DeviceMetric() {
    JsonSerializer<DeviceMetric> serializer = new JsonSerializer<>();
    JsonDeserializer<DeviceMetric> deserializer = new JsonDeserializer<>(DeviceMetric.class);
    return Serdes.serdeFrom(serializer, deserializer);
  }

  public static Serde<DeviceMetricCalculator> DeviceMetricCalculator() {
    JsonSerializer<DeviceMetricCalculator> serializer = new JsonSerializer<>();
    JsonDeserializer<DeviceMetricCalculator> deserializer =
        new JsonDeserializer<>(DeviceMetricCalculator.class);
    return Serdes.serdeFrom(serializer, deserializer);
  }
}
