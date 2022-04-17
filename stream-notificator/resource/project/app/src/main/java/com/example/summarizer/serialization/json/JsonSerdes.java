package com.example.summarizer.serialization.json;

import com.example.summarizer.model.DeviceMetric;
import com.example.summarizer.model.DeviceMetricAbnormalDetector;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public class JsonSerdes {
  public static Serde<DeviceMetric> DeviceMetric() {
    JsonSerializer<DeviceMetric> serializer = new JsonSerializer<>();
    JsonDeserializer<DeviceMetric> deserializer = new JsonDeserializer<>(DeviceMetric.class);
    return Serdes.serdeFrom(serializer, deserializer);
  }

  public static Serde<DeviceMetricAbnormalDetector> DeviceMetricAbnormalDetector() {
    JsonSerializer<DeviceMetricAbnormalDetector> serializer = new JsonSerializer<>();
    JsonDeserializer<DeviceMetricAbnormalDetector> deserializer =
        new JsonDeserializer<>(DeviceMetricAbnormalDetector.class);
    return Serdes.serdeFrom(serializer, deserializer);
  }
}
