package com.codecamp.imagerecognition.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sarsovsk on 15.10.2016.
 */
@Service
public class ImageRecognitionService {

    @Value("${imageRecognition.imagesFolderPath}")
    private String imagesFolderPath;

    public String storeImage(MultipartFile file) {

        String status;
        String imageType = getImageType(file);

        try {
            File destination = new File(String.format("%stest.%s", imagesFolderPath, imageType));
            BufferedImage src = ImageIO.read(file.getInputStream());
            ImageIO.write(src, imageType, destination);

            status = "Image successfully uploaded";

        } catch (IOException e) {
            e.printStackTrace();
            status = "Error uploading image";
        }

        return status;
    }

    private String getImageType(MultipartFile file) {

        String contentType = file.getContentType();

        Pattern imageTypePattern = Pattern.compile("image/(.+)");
        Matcher matcher = imageTypePattern.matcher(contentType);

        if (!matcher.matches()) {
            throw new RuntimeException("incorrect image format");
        }

        return matcher.group(1);
    }
}
