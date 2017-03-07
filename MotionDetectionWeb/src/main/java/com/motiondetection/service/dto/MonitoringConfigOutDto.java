package com.motiondetection.service.dto;

/**
 * Camera monitoring configuration DTO.
 */
public class MonitoringConfigOutDto {

  private String deviceId;

  private String delta_thresh;
  private String minimumMotionFrames;
  private int resolutionWidth;
  private int resolutionHeight;

  private String statusFontColor;
  private String timestampFontColor;
  private String motionIndicatorColor;

  private Boolean active;

  // Default values
  public MonitoringConfigOutDto() {
    this.delta_thresh = "0.5";
    this.minimumMotionFrames = "4";
    this.resolutionWidth = 768;
    this.resolutionHeight = 540;
    this.active = true;
    this.statusFontColor = "#DD4444";
    this.timestampFontColor = "#44DD44";
    this.motionIndicatorColor = "#4444DD";
  }

  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public String getMinimumMotionFrames() {
    return minimumMotionFrames;
  }

  public void setMinimumMotionFrames(Integer minimumMotionFrames) {
    this.minimumMotionFrames = String.valueOf(minimumMotionFrames);
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

  public String getDelta_thresh() {
    return delta_thresh;
  }

  public void setDelta_thresh(Float delta_thresh) {
    this.delta_thresh = String.valueOf(delta_thresh);
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

  public Boolean getActive() {
    return active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }
}
