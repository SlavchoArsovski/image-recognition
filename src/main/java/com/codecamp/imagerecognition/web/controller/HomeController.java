package com.codecamp.imagerecognition.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import javax.servlet.http.HttpServletRequest;
import java.io.Writer;

/**
 * Controller for home page.
 */
@Controller
@RequestMapping("/home")
public class HomeController {

    public static final String HOME_VIEW_NAME = "home";

    public static final String VIEW_BEAN = "viewBean";

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
    public String uploadImage(@RequestParam(name= "file") MultipartFile file) {

        System.out.println("image emptiness " + file.isEmpty());
        return HOME_VIEW_NAME;
    }
}
