package com.motiondetection.web.controller;

import com.motiondetection.enumeration.UploadStatus;
import com.motiondetection.service.MotionDetectionService;
import com.motiondetection.service.dto.ImageSearchDto;
import com.motiondetection.service.dto.LastUpdateDto;
import com.motiondetection.service.dto.StoredImagesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Controller for home page.
 */
@Controller
@RequestMapping("/home")
public class HomeController {

    private static final String HOME_VIEW_NAME = "home";
    private static final String SERVLET_CONTEXT_PATH = "SERVLET_CONTEXT_PATH";
    private static final String CLIENT_LIST = "clients";

    @Value("#{servletContext.contextPath}")
    private String servletContextPath;

    @Autowired
    private MotionDetectionService motionDetectionService;

    /**
     * Binds {@link ByteArrayMultipartFileEditor}.
     *
     * @param binder the web data binder.
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }

    /**
     * Handler for the motion detection home page.
     *
     * @return model and view.
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView home() {

        ModelAndView modelAndView = new ModelAndView(HOME_VIEW_NAME);
        modelAndView.addObject(SERVLET_CONTEXT_PATH, servletContextPath);

        List<String> clientList = motionDetectionService.getClientList();
        modelAndView.addObject(CLIENT_LIST, clientList);

        return modelAndView;
    }

    /**
     * Upload image captured from client.
     *
     * @param file the image uploaded from the client.
     * @param clientId the client id.
     * @return the upload status.
     */
    @RequestMapping(value="/upload", method = RequestMethod.POST)
    @ResponseBody
    public UploadStatus uploadImage(
        @RequestParam(name= "image") MultipartFile file,
        @RequestParam(name= "clientId", required = false) String clientId) {

        return motionDetectionService.storeImage(file, clientId);
    }

    /**
     * Returns the timestamp of last uploaded image for specific client.
     *
     * @param clientId the client id.
     * @return {@link LastUpdateDto}.
     */
    @RequestMapping(value = "/getLastUpdate", method = RequestMethod.GET)
    @ResponseBody
    public LastUpdateDto getLastUpdateForGivenClient(
        @RequestParam(name= "clientId", required = false) String clientId) {

        LastUpdateDto lastUpdateDto = new LastUpdateDto();

        String lastUpdate = motionDetectionService.getLastUpdateForClient(clientId);
        lastUpdateDto.setLastUpdate(lastUpdate);

        return lastUpdateDto;
    }

    /**
     * Get images according to the filters from the GUI.
     *
     * @param date the date of the image.
     * @param timeFrom the time from of the day range.
     * @param timeTo the time to of the day range.
     * @param clientId the client id.
     * @param pageNumber the page number.
     * @param lastUpdate last update received by the client.
     *
     * @return {@link StoredImagesDto} view model.
     */
    @RequestMapping(value = "/getImages", method = RequestMethod.GET)
    @ResponseBody
    public StoredImagesDto getImages(
        @RequestParam(name= "date", required = false) String date,
        @RequestParam(name= "timeFrom", required = false) String timeFrom,
        @RequestParam(name= "timeTo", required = false) String timeTo,
        @RequestParam(name= "clientId", required = false) String clientId,
        @RequestParam(name= "pageNumber", required = false) Long pageNumber,
        @RequestParam(name= "lastUpdate", required = false) String lastUpdate) {

        ImageSearchDto imageSearchDto = new ImageSearchDto();
        imageSearchDto.setDate(date);
        imageSearchDto.setTimeFrom(timeFrom);
        imageSearchDto.setTimeTo(timeTo);
        imageSearchDto.setClientId(clientId);
        imageSearchDto.setPageNumber(pageNumber);
        imageSearchDto.setLastUpdate(lastUpdate);

        StoredImagesDto storedImages = motionDetectionService.getStoredImages(imageSearchDto);

        return storedImages;
    }
}
