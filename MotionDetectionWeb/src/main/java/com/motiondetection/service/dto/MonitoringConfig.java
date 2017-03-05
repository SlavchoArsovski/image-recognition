package com.motiondetection.service.dto;

/**
 * Camera monitoring configuration DTO.
 */
public class MonitoringConfig {

  private String deviceId;

  private Float sensitivity;
  private Integer minimumMotionFrames;
  private String resolution;

  private String statusFontColor;
  private String timestampFontColor;
  private String motionIndicatorColor;

  // Default values
  public MonitoringConfig() {
    this.sensitivity = 0.5f;
    this.minimumMotionFrames = 4;
    this.resolution = "768x540";
  }

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public Float getSensitivity() {
    return sensitivity;
  }

  public void setSensitivity(Float sensitivity) {
    this.sensitivity = sensitivity;
  }

  public Integer getMinimumMotionFrames() {
    return minimumMotionFrames;
  }

  public void setMinimumMotionFrames(Integer minimumMotionFrames) {
    this.minimumMotionFrames = minimumMotionFrames;
  }

  public String getResolution() {
    return resolution;
  }

  public void setResolution(String resolution) {
    this.resolution = resolution;
  }

  public String getStatusFontColor() {
    return statusFontColor;
  }

  public void setStatusFontColor(String statusFontColor) {
    this.statusFontColor = statusFontColor;
  }

  public String getTimestampFontColor() {
    return timestampFontColor;
  }

  public void setTimestampFontColor(String timestampFontColor) {
    this.timestampFontColor = timestampFontColor;
  }

  public String getMotionIndicatorColor() {
    return motionIndicatorColor;
  }

  public void setMotionIndicatorColor(String motionIndicatorColor) {
    this.motionIndicatorColor = motionIndicatorColor;
  }
}
