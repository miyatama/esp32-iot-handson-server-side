package com.example.summarizer.model;

import org.json.JSONObject;

public class DeviceMetric {
  private String deviceId;
  private Long moist;
  private Long light;

  public Long getMoist() {
    return this.moist;
  }

  public Long getLight() {
    return this.light;
  }

  public String getDeviceId() {
    return this.deviceId;
  }

  public static DeviceMetric parse(String jsonText) {
    try {
      JSONObject json = new JSONObject(jsonText);
      if (!json.has("deviceId") || !json.has("moist") || !json.has("light")) {
        return new DeviceMetric();
      }
      return new DeviceMetric(
          json.getString("deviceId"),
          json.getNumber("moist").longValue(),
          json.getNumber("light").longValue());
    } catch (Exception e) {
      return new DeviceMetric();
    }
  }

  public String stringify() {
    return "{\"deviceId\": \""
        + getDeviceId()
        + "\""
        + ", \"moist\": "
        + getMoist()
        + ", \"light\": "
        + getLight()
        + " }";
  }

  public DeviceMetric() {
    this.deviceId = "";
    this.moist = 0L;
    this.light = 0L;
  }

  public DeviceMetric(String deviceId, Long moist, Long light) {
    this.deviceId = deviceId;
    this.moist = moist;
    this.light = light;
  }

  @Override
  public String toString() {
    return "{"
        + "deviceId='"
        + getDeviceId()
        + "'"
        + ", moist='"
        + getMoist()
        + "', light='"
        + getLight()
        + "'"
        + "}";
  }
}
