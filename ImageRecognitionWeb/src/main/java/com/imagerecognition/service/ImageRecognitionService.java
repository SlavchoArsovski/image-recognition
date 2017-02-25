package com.imagerecognition.service;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sarsovsk on 15.10.2016.
 */
@Service
public class ImageRecognitionService implements ApplicationContextAware {

    @Value("${imageRecognition.imagesFolderPath}")
    private String imagesFolderPath;

    private ApplicationContext applicationContext;

    public String storeImage(MultipartFile file) {

        String status;
        String imageType = getImageType(file);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
        String formatDateTime = now.format(formatter);

        try {
            // File destination = new File(String.format("%simage-%s.%s", imagesFolderPath, formatDateTime, imageType));
            File destination = new File(String.format("%simageRecognised.%s", imagesFolderPath, imageType));
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

    public String getStoredImage() {

        String imagePath =
                String.format(
                        "file:%simageRecognised.%s",
                        imagesFolderPath,
                        "jpeg");

        byte[] byteArray;

        try {
            Resource resource = applicationContext.getResource(imagePath);
            byteArray = IOUtils.toByteArray(resource.getInputStream());
        } catch (IOException e) {
            byteArray = getFallbackImage();
        }

        return Base64.encode(byteArray);
    }

    private byte[] getFallbackImage() {
        Resource resource = applicationContext.getResource("classpath:nophoto.jpeg");
        try {
            return IOUtils.toByteArray(resource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("failed to load fallback image");
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
