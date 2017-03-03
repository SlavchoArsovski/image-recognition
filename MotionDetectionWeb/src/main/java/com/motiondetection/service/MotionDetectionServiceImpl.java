package com.motiondetection.service;

import com.motiondetection.enumeration.UploadStatus;
import com.motiondetection.service.dto.ImageSearchDto;
import com.motiondetection.service.dto.StoredImagesDto;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.lang3.StringUtils;
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
import java.io.FileFilter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of {@link MotionDetectionService}.
 */
@Service
public class MotionDetectionServiceImpl implements MotionDetectionService, ApplicationContextAware {

  private static final String IMAGE_FILE_REGEX =
      "image-\\d{4}-\\d{2}-\\d{2}-\\d{2}-\\d{2}-\\d{2}.(jpeg|jpg|gif|png|bmp)";

  private static final String FULL_TIMESTAMP_PATTERN = "yyyy-MM-dd-HH-mm-ss";
  private static final DateTimeFormatter fullTimeStampFormatter = DateTimeFormatter.ofPattern(FULL_TIMESTAMP_PATTERN);

  private static final String DATE_PATTERN = "yyyy-MM-dd";
  private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

  @Value("${imageRecognition.imagesFolderPath}")
  private String imagesFolderPath;

  @Value("${imageRecognition.maxImages}")
  private int maxImages;

  private ApplicationContext applicationContext;

  @Override
  public UploadStatus storeImage(MultipartFile file) {

    UploadStatus status;
    String imageType = getImageType(file);

    LocalDateTime currentDateTime = LocalDateTime.now();
    String currentDateTimeFormatted = currentDateTime.format(fullTimeStampFormatter);
    String currentDateFormatted = currentDateTime.format(dateFormatter);

    try {

      String imageDirectoryPath = String.format("%s%s", imagesFolderPath, currentDateFormatted);
      File directory = new File(imageDirectoryPath);
      if (!directory.exists()){
        directory.mkdir();
      }

      String fileName = String.format("%s\\image-%s.%s", imageDirectoryPath, currentDateTimeFormatted, imageType);

      File destination = new File(fileName);
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

    Pattern imageTypePattern = Pattern.compile("image/(jpeg|jpg|gif|png|bmp)");
    Matcher matcher = imageTypePattern.matcher(contentType);

    if (!matcher.matches()) {
      throw new RuntimeException("incorrect image format");
    }

    return matcher.group(1);
  }

  @Override
  public StoredImagesDto getStoredImages(ImageSearchDto imageSearchDto) {

    String imageDirectory = resolveImageDirectory(imageSearchDto);
    File storedImagesDir = new File(imageDirectory);
    FileFilter regexFileFilter = new RegexFileFilter(IMAGE_FILE_REGEX);
    File[] files = storedImagesDir.listFiles(regexFileFilter);

    StoredImagesDto dto = new StoredImagesDto();

    if (files != null)  {

      Arrays.sort(files, new ImageFilesComparator());

      for (File file : files) {
        String imageAsEncodedString = getImageAsEncodedString(file);
        dto.addImageEncoded(imageAsEncodedString);
      }
    }

    return dto;
  }

  private String resolveImageDirectory(ImageSearchDto imageSearchDto) {

    String date = imageSearchDto.getDate();
    String directory;

    if (StringUtils.isNotBlank(date) && date.matches("\\d{4}-\\d{2}-\\d{2}")) {
      directory = String.format("%s%s", imagesFolderPath, date);
    } else {
      LocalDateTime currentDateTime = LocalDateTime.now();
      String currentDateFormatted = currentDateTime.format(dateFormatter);
      directory = String.format("%s%s", imagesFolderPath, currentDateFormatted);
    }

    return directory;
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
