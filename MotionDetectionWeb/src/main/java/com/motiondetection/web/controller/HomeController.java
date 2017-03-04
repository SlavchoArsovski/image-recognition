package com.motiondetection.web.controller;

import com.motiondetection.enumeration.UploadStatus;
import com.motiondetection.service.MotionDetectionService;
import com.motiondetection.service.dto.ImageSearchDto;
import com.motiondetection.service.dto.MonitoringConfig;
import com.motiondetection.service.dto.StoredImagesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for home page.
 */
@Controller
@RequestMapping("/home")
public class HomeController {

    private static final String HOME_VIEW_NAME = "home";
    private static final String SERVLET_CONTEXT_PATH = "SERVLET_CONTEXT_PATH";

    @Value("#{servletContext.contextPath}")
    private String servletContextPath;

    @Autowired
    private MotionDetectionService motionDetectionService;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView home() {

        ModelAndView modelAndView = new ModelAndView(HOME_VIEW_NAME);
        modelAndView.addObject(SERVLET_CONTEXT_PATH, servletContextPath);

        return modelAndView;
    }

    @RequestMapping(value="/upload", method = RequestMethod.POST)
    @ResponseBody
    public UploadStatus uploadImage(@RequestParam(name= "image") MultipartFile file) {
        return motionDetectionService.storeImage(file);
    }

    @RequestMapping(value = "/getImages", method = RequestMethod.GET)
    @ResponseBody
    public StoredImagesDto getImages(
        @RequestParam(name= "date", required = false) String date,
        @RequestParam(name= "timeFrom", required = false) String timeFrom,
        @RequestParam(name= "timeTo", required = false) String timeTo,
        @RequestParam(name= "clientId", required = false) String clientId) {

        ImageSearchDto imageSearchDto = new ImageSearchDto();
        imageSearchDto.setDate(date);
        imageSearchDto.setClientId(clientId);

        StoredImagesDto storedImages = motionDetectionService.getStoredImages(imageSearchDto);

        return storedImages;
    }

    @RequestMapping(value = "/getConfig", method = RequestMethod.GET)
    @ResponseBody
    public MonitoringConfig getConfig() {
        return new MonitoringConfig();
    }

    @ResponseBody
    @RequestMapping(value="/registerDevice", method = RequestMethod.GET)
    public String registerDevice() {
        // TODO: Implement
        return null;
    }
}
