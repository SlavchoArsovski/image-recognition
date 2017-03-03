package com.motiondetection.service;

import com.motiondetection.enumeration.ConfigurationParameter;
import com.motiondetection.service.dto.MonitoringConfig;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to manipulate the configuration data.
 */
@Service
public class ConfigurationService {

  private Map<String, MonitoringConfig> configMap;

  public ConfigurationService() {
    configMap = new HashMap<>();
  }

  /**
   * Returns the configuration data for the device with the specified id.
   * @param id - the unique id of the device
   * @return the device-specific config data
   */
  public MonitoringConfig getMonitoringConfigForDeviceId(String id) {
    return configMap.get(id);
  }

  /**
   * Dropdown data options for the view.
   * @return the options for the user.
   */
  public Map<ConfigurationParameter, List<String>> getConfigOptions() {
    Map<ConfigurationParameter, List<String>> configDropdownData = new HashMap<>();
    configDropdownData.put(ConfigurationParameter.RESOLUTION_OPTIONS, getResolutionOptions());
    configDropdownData.put(ConfigurationParameter.MINIMUM_MOTION_FRAMES_OPTIONS, getMinimumMotionFramesOptions());
    configDropdownData.put(ConfigurationParameter.SENSITIVITY_OPTIONS, getSensitivityOptions());
    return configDropdownData;
  }

  private List<String> getResolutionOptions() {
    List<String> resolutionOptions = new ArrayList<>();
    resolutionOptions.add("480x300");
    resolutionOptions.add("512x342");
    resolutionOptions.add("640x380");
    resolutionOptions.add("768x480");
    resolutionOptions.add("800x600");
    resolutionOptions.add("960x660");
    resolutionOptions.add("1024x768");
    resolutionOptions.add("1280x854");
    resolutionOptions.add("1600x1200");
    return resolutionOptions;
  }

  private List<String> getMinimumMotionFramesOptions() {
    List<String> minimumMotionFramesOptions = new ArrayList<>();
    minimumMotionFramesOptions.add("4");
    minimumMotionFramesOptions.add("5");
    minimumMotionFramesOptions.add("6");
    minimumMotionFramesOptions.add("7");
    minimumMotionFramesOptions.add("8");
    return minimumMotionFramesOptions;
  }

  private List<String> getSensitivityOptions() {
    List<String> sensitivityOptions = new ArrayList<>();
    sensitivityOptions.add("2.0");
    sensitivityOptions.add("2.5");
    sensitivityOptions.add("3.0");
    sensitivityOptions.add("3.5");
    sensitivityOptions.add("4.0");
    sensitivityOptions.add("4.5");
    sensitivityOptions.add("5.0");
    return sensitivityOptions;
  }
}
