package com.motiondetection.enumeration;

/**
 * Configuration parameters.
 */
public enum ConfigurationParameter {
  /** Resolution */
  RESOLUTION_OPTIONS("resolution"),
  /** Sensitivity options */
  SENSITIVITY_OPTIONS("sensitivity"),
  /** Minimum motion frames */
  MINIMUM_MOTION_FRAMES_OPTIONS("minimumMotionFrames");

  private String value;

  private ConfigurationParameter(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
