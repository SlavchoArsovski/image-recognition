package com.motiondetection.service.dto;

/**
 * Camera monitoring configuration DTO.
 */
public class MonitoringConfigOutDto {

  private String deviceId;

  private Float delta_thresh;
  private Integer minimumMotionFrames;
  private int resolutionWidth;
  private int resolutionHeight;

  private String statusFontColor;
  private String timestampFontColor;
  private String motionIndicatorColor;

  // Default values
  public MonitoringConfigOutDto() {
    this.delta_thresh = 0.5f;
    this.minimumMotionFrames = 4;
    this.resolutionWidth = 768;
    this.resolutionHeight = 540;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public Integer getMinimumMotionFrames() {
    return minimumMotionFrames;
  }

  public void setMinimumMotionFrames(Integer minimumMotionFrames) {
    this.minimumMotionFrames = minimumMotionFrames;
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

  public Float getDelta_thresh() {
    return delta_thresh;
  }

  public void setDelta_thresh(Float delta_thresh) {
    this.delta_thresh = delta_thresh;
  }

  public int getResolutionWidth() {
    return resolutionWidth;
  }

  public void setResolutionWidth(int resolutionWidth) {
    this.resolutionWidth = resolutionWidth;
  }

  public int getResolutionHeight() {
    return resolutionHeight;
  }

  public void setResolutionHeight(int resolutionHeight) {
    this.resolutionHeight = resolutionHeight;
  }
}
