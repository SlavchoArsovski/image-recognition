package com.motiondetection.web.controller;

import com.motiondetection.enumeration.ConfigurationParameter;
import com.motiondetection.service.ConfigurationService;
import com.motiondetection.service.dto.MonitoringConfig;
import com.motiondetection.service.dto.MonitoringConfigOutDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Configuration controller.
 */
@Controller
@RequestMapping("/config")
public class ConfigController {

  public static final String CONFIG_PAGE_PATH = "configPage";

  @Autowired
  private ConfigurationService configurationService;

  @RequestMapping(method = RequestMethod.GET)
  public String getConfigPageForDeviceId(@RequestParam(required = false) String deviceId, Model model) {

    // Dropdown options
    initModel(model);

    // Current selection values
    model.addAttribute("configMap", configurationService.getConfigMap());

    // Model init
    MonitoringConfig config = null;
    if (deviceId != null) {
      config = configurationService.getMonitoringConfigForDeviceId(deviceId);
    } else {
      List<MonitoringConfig> configList = new ArrayList<>(configurationService.getConfigMap().values());
      if (configList.get(0) == null) {
        config = new MonitoringConfig();
      } else {
        config = configList.get(0);
      }
    }
    model.addAttribute("monitoringConfig", config);

    return CONFIG_PAGE_PATH;
  }

  @RequestMapping(method = RequestMethod.POST)
  public String saveConfig(
          @ModelAttribute("monitoringConfig") MonitoringConfig updatedConfig,
          BindingResult bindingResult,
          Model model) {

    initModel(model);

    if (bindingResult.hasErrors()) {
      return CONFIG_PAGE_PATH;
    }

    configurationService.updateDeviceConfig(updatedConfig);

    model.addAttribute("monitoringConfig", updatedConfig);

    return CONFIG_PAGE_PATH;
  }

  private void initModel(Model model) {
    Map<ConfigurationParameter, List<Object>> configOptions = configurationService.getConfigOptions();
    model.addAttribute(
            ConfigurationParameter.SENSITIVITY_OPTIONS.getValue(),
            configOptions.get(ConfigurationParameter.SENSITIVITY_OPTIONS));
    model.addAttribute(
            ConfigurationParameter.MINIMUM_MOTION_FRAMES_OPTIONS.getValue(),
            configOptions.get(ConfigurationParameter.MINIMUM_MOTION_FRAMES_OPTIONS));
    model.addAttribute(
            ConfigurationParameter.RESOLUTION_OPTIONS.getValue(),
            configOptions.get(ConfigurationParameter.RESOLUTION_OPTIONS));
    model.addAttribute("devices", configurationService.getDeviceIds());
  }

  @RequestMapping(value = "/getConfig", method = RequestMethod.GET)
  @ResponseBody
  public MonitoringConfigOutDto getConfig(@RequestParam String deviceId) {
    MonitoringConfig config = configurationService.getMonitoringConfigForDeviceId(deviceId);
    return configurationService.mapMonitoringConfig(config);
  }
}
