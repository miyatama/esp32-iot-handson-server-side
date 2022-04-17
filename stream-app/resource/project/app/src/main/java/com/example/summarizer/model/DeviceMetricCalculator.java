package com.example.summarizer.model;

public class DeviceMetricCalculator {
  public Long moistSummary = 0L;
  public Long lightSummary = 0L;
  public Long dataCount = 0L;

  public DeviceMetricCalculator() {}

  public void add(Long moist, Long light) {
    moistSummary += moist;
    lightSummary += light;
    dataCount++;
  }

  public long calcMoist() {
    return (long) Math.floor(Double.valueOf(moistSummary) / Double.valueOf(dataCount));
  }

  public long calcLight() {
    return (long) Math.floor(Double.valueOf(lightSummary) / Double.valueOf(dataCount));
  }

  @Override
  public String toString() {
    return "{"
        + "moist_summary='"
        + moistSummary
        + "'"
        + ", light_summary='"
        + lightSummary
        + "'"
        + ", data_count='"
        + dataCount
        + "'"
        + "}";
  }
}
