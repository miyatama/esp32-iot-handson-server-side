package com.example.summarizer.model;

public class DeviceMetricAbnormalDetector {
  public Long moist = 0L;
  public Long light = 0L;
  public Boolean abnormal = false;

  public DeviceMetricAbnormalDetector() {}

  public void detect(Long newMoist, Long newLight) {
    if ((newLight - light) > 1000L) {
      abnormal = true;
    } else {
      abnormal = false;
    }
    light = newLight;
    moist = newMoist;
  }

  @Override
  public String toString() {
    return "{"
        + "moist='"
        + moist
        + "'"
        + ", light='"
        + light
        + "'"
        + ", abnormal='"
        + abnormal
        + "'"
        + "}";
  }
}
