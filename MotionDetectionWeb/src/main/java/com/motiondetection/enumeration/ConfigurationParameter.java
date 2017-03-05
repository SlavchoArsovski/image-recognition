package com.motiondetection.enumeration;

/**
 * Configuration parameters.
 */
public enum ConfigurationParameter {
  /** Resolution */
  RESOLUTION_OPTIONS("resolutionOptions"),
  /** Sensitivity options */
  SENSITIVITY_OPTIONS("sensitivityOptions"),
  /** Minimum motion frames */
  MINIMUM_MOTION_FRAMES_OPTIONS("minimumMotionFramesOptions");

  private String value;

  ConfigurationParameter(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
