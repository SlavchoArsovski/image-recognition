package com.motiondetection.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.motiondetection.enumaration.UploadStatus;
import com.motiondetection.service.dto.StoredImagesDto;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * Implementation of {@link MotionDetectionService}.
 */
@Service
public class MotionDetectionServiceImpl implements MotionDetectionService, ApplicationContextAware {

  private static final String IMAGE_FILE_REGEX =
      "image-\\d{4}-\\d{2}-\\d{2}-\\d{2}-\\d{2}-\\d{2}.(jpeg|jpg|gif|png|bmp)";

  @Value("${imageRecognition.imagesFolderPath}")
  private String imagesFolderPath;

  @Value("${imageRecognition.maxImages}")
  private int maxImages;


  private ApplicationContext applicationContext;

  @Override
  public UploadStatus storeImage(MultipartFile file) {

    UploadStatus status;
    String imageType = getImageType(file);

    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss");
    String formatDateTime = now.format(formatter);

    try {
      File destination = new File(String.format("%simage-%s.%s", imagesFolderPath, formatDateTime, imageType));
      BufferedImage src = ImageIO.read(file.getInputStream());
      ImageIO.write(src, imageType, destination);

      status = UploadStatus.SUCCESS;

    } catch (IOException e) {
      e.printStackTrace();
      status = UploadStatus.FAIL;
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

  @Override
  public StoredImagesDto getStoredImages() {

    File storedImagesDir = new File(imagesFolderPath);
    FileFilter regexFileFilter = new RegexFileFilter(IMAGE_FILE_REGEX);
    File[] files = storedImagesDir.listFiles(regexFileFilter);

    Arrays.sort(files, new ImageFilesComparator());

    StoredImagesDto storedImagesDto = new StoredImagesDto();

    for (int i = 0, length = Math.min(files.length, maxImages); i < length; i++) {

      String imageAsEncodedString = getImageAsEncodedString(files[i]);
      storedImagesDto.addImageEncoded(imageAsEncodedString);
    }

    return storedImagesDto;
  }

  private String getImageAsEncodedString(File file) {

    byte[] byteArray;

    try {
      String imagePath = String.format("file:%s", file.getAbsolutePath());
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
