package com.motiondetection.service;

import com.motiondetection.enumeration.ConfigurationParameter;
import com.motiondetection.service.dto.MonitoringConfig;
import com.motiondetection.service.dto.MonitoringConfigOutDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to manipulate the configuration data.
 */
@Service
public class ConfigurationService {

  private Map<String, MonitoringConfig> configMap;

  @Value("${clientList}")
  private String clientList;

  private List<String> getClientList() {
    String[] clients = clientList.split(",");
    return new ArrayList<>(Arrays.asList(clients));
  }

  @PostConstruct
  public void init() {
    configMap = new HashMap<>();
    for(String deviceId : getClientList()) {
      MonitoringConfig config = new MonitoringConfig();
      config.setDeviceId(deviceId);
      configMap.put(deviceId, config);
    }
  }

  /**
   * Returns the configuration data for the device with the specified id.
   * @param id - the unique id of the device
   * @return the device-specific config data
   */
  public MonitoringConfig getMonitoringConfigForDeviceId(String id) {
    MonitoringConfig monitoringConfig = configMap.get(id);
    if (monitoringConfig == null) {
      monitoringConfig = new MonitoringConfig();
    }
    return monitoringConfig;
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
    return new ArrayList<>(configMap.keySet());
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

  /**
   * Maps the config to the out dto.
   */
  public MonitoringConfigOutDto mapMonitoringConfig(MonitoringConfig inDto) {
    MonitoringConfigOutDto outDto = new MonitoringConfigOutDto();
    outDto.setDelta_thresh(inDto.getSensitivity());
    outDto.setDeviceId(inDto.getDeviceId());
    outDto.setMinimumMotionFrames(inDto.getMinimumMotionFrames());
    outDto.setMotionIndicatorColor(inDto.getMotionIndicatorColor());
    String[] resolution = inDto.getResolution().split("x");
    outDto.setResolutionHeight(Integer.valueOf(resolution[1]));
    outDto.setResolutionWidth(Integer.valueOf(resolution[0]));
    outDto.setStatusFontColor(inDto.getStatusFontColor());
    outDto.setTimestampFontColor(inDto.getTimestampFontColor());
    outDto.setActive(inDto.getActive());
    return outDto;
  }
}
