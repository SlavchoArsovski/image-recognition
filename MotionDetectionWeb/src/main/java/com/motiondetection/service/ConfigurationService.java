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
    MonitoringConfig configRealCamera = new MonitoringConfig();
    configRealCamera.setDeviceId("realCameraRPi");
    configMap.put("realCameraRPi", configRealCamera);
    MonitoringConfig configMockCamera1 = new MonitoringConfig();
    configMockCamera1.setDeviceId("mockCamera1");
    configMap.put("mockCamera1", configMockCamera1);
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
   * Returns the configuration map (deviceId -> config).
   */
  public Map<String, MonitoringConfig> getConfigMap() {
    return configMap;
  }

  /**
   * Returns a list of ids of the registered devices.
   */
  public List<String> getDeviceIds() {
    return new ArrayList<String>(configMap.keySet());
  }

  public void updateDeviceConfig(MonitoringConfig newConfig) {
    if (configMap.get(newConfig.getDeviceId()) != null) {
      configMap.put(newConfig.getDeviceId(), newConfig);
    } else {
      System.out.println("The device id is not registered");
    }
  }

  /**
   * Dropdown data options for the view.
   * @return the options for the user.
   */
  public Map<ConfigurationParameter, List<Object>> getConfigOptions() {
    Map<ConfigurationParameter, List<Object>> configDropdownData = new HashMap<>();
    configDropdownData.put(ConfigurationParameter.RESOLUTION_OPTIONS, getResolutionOptions());
    configDropdownData.put(ConfigurationParameter.MINIMUM_MOTION_FRAMES_OPTIONS, getMinimumMotionFramesOptions());
    configDropdownData.put(ConfigurationParameter.SENSITIVITY_OPTIONS, getSensitivityOptions());
    return configDropdownData;
  }

  private List<Object> getResolutionOptions() {
    List<Object> resolutionOptions = new ArrayList<>();
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

  private List<Object> getMinimumMotionFramesOptions() {
    List<Object> minimumMotionFramesOptions = new ArrayList<>();
    minimumMotionFramesOptions.add(4);
    minimumMotionFramesOptions.add(5);
    minimumMotionFramesOptions.add(6);
    minimumMotionFramesOptions.add(7);
    minimumMotionFramesOptions.add(8);
    return minimumMotionFramesOptions;
  }

  private List<Object> getSensitivityOptions() {
    List<Object> sensitivityOptions = new ArrayList<>();
    sensitivityOptions.add(2.0f);
    sensitivityOptions.add(2.5f);
    sensitivityOptions.add(3.0f);
    sensitivityOptions.add(3.5f);
    sensitivityOptions.add(4.0f);
    sensitivityOptions.add(4.5f);
    sensitivityOptions.add(5.0f);
    return sensitivityOptions;
  }
}
