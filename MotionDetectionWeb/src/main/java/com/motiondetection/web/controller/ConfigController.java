package com.motiondetection.web.controller;

import com.motiondetection.enumeration.ConfigurationParameter;
import com.motiondetection.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * Configuration controller.
 */
@Controller
@RequestMapping("/config")
public class ConfigController {

  @Autowired
  private ConfigurationService configurationService;

  @RequestMapping(method = RequestMethod.GET)
  public ModelAndView getConfigPage() {
    ModelAndView modelAndView = new ModelAndView();
    Map<ConfigurationParameter, List<String>> configOptions = configurationService.getConfigOptions();
    modelAndView.addObject(
            ConfigurationParameter.SENSITIVITY_OPTIONS.getValue(),
            configOptions.get(ConfigurationParameter.SENSITIVITY_OPTIONS));
    modelAndView.addObject(
            ConfigurationParameter.MINIMUM_MOTION_FRAMES_OPTIONS.getValue(),
            configOptions.get(ConfigurationParameter.MINIMUM_MOTION_FRAMES_OPTIONS));
    modelAndView.addObject(
            ConfigurationParameter.RESOLUTION_OPTIONS.getValue(),
            configOptions.get(ConfigurationParameter.RESOLUTION_OPTIONS));

    modelAndView.setViewName("configPage");
    return modelAndView;
  }

}
