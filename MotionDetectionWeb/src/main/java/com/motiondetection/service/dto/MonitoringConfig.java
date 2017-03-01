package com.motiondetection.service.dto;

/**
 * Camera monitoring configuration DTO.
 */
public class MonitoringConfig {

  private float sensitivity;
  private float delayBetweenUploads;
  private int minimumMotionFrames;
  private float deltaThresh;
  private int resolutionWidth;
  private int resolutionHeight;

  // Default values
  public MonitoringConfig() {
    this.sensitivity = 0.5f;
    this.delayBetweenUploads = 2.5f;
    this.minimumMotionFrames = 4;
    this.deltaThresh = 3.5f;
    this.resolutionWidth = 768;
    this.resolutionHeight = 540;
  }

  public float getSensitivity() {
    return sensitivity;
  }

  public void setSensitivity(float sensitivity) {
    this.sensitivity = sensitivity;
  }

  public float getDelayBetweenUploads() {
    return delayBetweenUploads;
  }

  public void setDelayBetweenUploads(float delayBetweenUploads) {
    this.delayBetweenUploads = delayBetweenUploads;
  }

  public int getMinimumMotionFrames() {
    return minimumMotionFrames;
  }

  public void setMinimumMotionFrames(int minimumMotionFrames) {
    this.minimumMotionFrames = minimumMotionFrames;
  }

  public float getDeltaThresh() {
    return deltaThresh;
  }

  public void setDeltaThresh(float deltaThresh) {
    this.deltaThresh = deltaThresh;
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
