package com.imagerecognition.web.controller;

import com.imagerecognition.service.ImageRecognitionService;
import com.imagerecognition.web.model.ImageViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

/**
 * Controller for home page.
 */
@Controller
@RequestMapping("/home")
public class HomeController {

    public static final String HOME_VIEW_NAME = "home";

    @Autowired
    private ImageRecognitionService imageRecognitionService;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }

    @RequestMapping(method = RequestMethod.GET)
    public String home() {
        return HOME_VIEW_NAME;
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @ResponseBody
    public String test(@RequestParam(name = "param") String param) {
        return "success receiving param = " + param;
    }

    @RequestMapping(value="/upload", method = RequestMethod.POST)
    @ResponseBody
    public String uploadImage(@RequestParam(name= "image") MultipartFile file) {
        return imageRecognitionService.storeImage(file);
    }

    @RequestMapping(value = "/getImage", method = RequestMethod.GET)
    @ResponseBody
    public ImageViewModel getImage() {

        String imageDataEncoded = imageRecognitionService.getStoredImage();

        ImageViewModel viewModel = new ImageViewModel();
        viewModel.setData(imageDataEncoded);

        return viewModel;
    }
}