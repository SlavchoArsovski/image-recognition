package com.motiondetection.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

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

import com.motiondetection.enumeration.UploadStatus;
import com.motiondetection.service.dto.ImageSearchDto;
import com.motiondetection.service.dto.StoredImagesDto;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * Implementation of {@link MotionDetectionService}.
 */
@Service
public class MotionDetectionServiceImpl implements MotionDetectionService, ApplicationContextAware {

  private static final String IMAGE_FILE_REGEX =
      "image-(\\d{4}-\\d{2}-\\d{2}-\\d{2}-\\d{2}-\\d{2})\\.(jpeg|jpg|gif|png|bmp)";

  private static final String DATE_PATTERN = "yyyy-MM-dd";
  private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

  private static final String FULL_TIMESTAMP_PATTERN = "yyyy-MM-dd-HH-mm-ss";
  private static final DateTimeFormatter fullTimeStampFormatter = DateTimeFormatter.ofPattern(FULL_TIMESTAMP_PATTERN);

  private static final Pattern fileNamePattern = Pattern.compile(IMAGE_FILE_REGEX);


  @Value("${motionDetection.imagesFolderPath}")
  private String imagesFolderPath;

  @Value("${clientList}")
  private String clientList;

  @Value("${pageSize}")
  private long pageSize;

  private Map<String, String> lastUpdates = new HashMap<>();

  private ApplicationContext applicationContext;

  @PostConstruct
  private void init() {

    List<String> clients = getClientList();
    String lastUpdate = LocalDateTime.now().format(fullTimeStampFormatter);

    clients.forEach(client -> {
      lastUpdates.put(client, lastUpdate);
    });

    lastUpdates.put("no_client", lastUpdate);
  }

  @Override
  public List<String> getClientList() {
    String[] clients = clientList.split(",");
    List<String> clintList = new ArrayList<>(Arrays.asList(clients));
    return clintList;
  }

  @Override
  public UploadStatus storeImage(MultipartFile file, String clientId) {

    UploadStatus status;
    String imageType = getImageType(file);

    LocalDateTime currentDateTime = LocalDateTime.now();
    String currentDateTimeFormatted = currentDateTime.format(fullTimeStampFormatter);
    String currentDateFormatted = currentDateTime.format(dateFormatter);

    try {

      String imageDirectoryPath = String.format("%s%s", imagesFolderPath, currentDateFormatted);

      if (StringUtils.isNotEmpty(clientId)) {
        imageDirectoryPath = String.format("%s\\%s", imageDirectoryPath, clientId);
      }

      File directory = new File(imageDirectoryPath);
      if (!directory.exists()){
        //noinspection ResultOfMethodCallIgnored
        directory.mkdirs();
      }

      String fileName = String.format("%s\\image-%s.%s", imageDirectoryPath, currentDateTimeFormatted, imageType);

      File destination = new File(fileName);
      BufferedImage src = ImageIO.read(file.getInputStream());
      ImageIO.write(src, imageType, destination);

      status = UploadStatus.SUCCESS;

      String lastUpdateKey = StringUtils.isNotBlank(clientId) ? clientId : "no_client";
      lastUpdates.put(lastUpdateKey, LocalDateTime.now().format(fullTimeStampFormatter));
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

    Long pageNumber = imageSearchDto.getPageNumber();

    if (pageNumber == null || pageNumber < 0) {
      pageNumber = 0L;
    }

    String clientId = imageSearchDto.getClientId();
    String lastUpdateKey = StringUtils.isNotBlank(clientId) ? clientId : "no_client";

    if (!StringUtils.equals(imageSearchDto.getLastUpdate(), lastUpdates.get(lastUpdateKey))) {
      pageNumber = 0L;
    }

    StoredImagesDto dto = new StoredImagesDto();
    dto.setLastUpdate(lastUpdates.get(lastUpdateKey));

    String imageDirectory = resolveImageDirectory(imageSearchDto);
    File storedImagesDir = new File(imageDirectory);
    FileFilter regexFileFilter = new RegexFileFilter(IMAGE_FILE_REGEX);
    File[] files = storedImagesDir.listFiles(regexFileFilter);

    if (files != null)  {

      int numberOfPages = files.length / (int) pageSize;

      if (files.length % (int) pageSize != 0) {
        numberOfPages += 1;
      }

      dto.setNumberOfPages(numberOfPages);

      String date = String.format("%s-00-00-00", imageSearchDto.getDate());
      LocalDateTime dateTime = LocalDateTime.parse(date, fullTimeStampFormatter);

      String timeFrom = imageSearchDto.getTimeFrom();
      if (StringUtils.isBlank(timeFrom)) {
        timeFrom = "00";
      }

      String timeTo = imageSearchDto.getTimeTo();
      if (StringUtils.isBlank(timeTo)) {
        timeTo = "24";
      }

      LocalDateTime from = dateTime.plusHours(Long.valueOf(timeFrom));
      LocalDateTime to = dateTime.plusHours(Long.valueOf(timeTo));

      System.out.println(from);
      System.out.println(to);

      Arrays
          .stream(files)
          .filter(file -> {

            Matcher matcher = fileNamePattern.matcher(file.getName());

            if (matcher.matches()) {
              LocalDateTime imageDateTime = LocalDateTime.parse(matcher.group(1), fullTimeStampFormatter);

              boolean isImageInSelectedDateTimeRange =
                  (imageDateTime.isAfter(from) || imageDateTime.isEqual(from)) && imageDateTime.isBefore(to);


              if (isImageInSelectedDateTimeRange) {
                return true;
              }

            }
            return false;
          })
          .sorted(new ImageFilesComparator())
          .skip(pageNumber * pageSize)
          .limit(pageSize)
          .forEach(file -> {
            String imageAsEncodedString = getImageAsEncodedString(file);
            dto.addImageEncoded(imageAsEncodedString);
          });
    }


    return dto;
  }

  private String resolveImageDirectory(ImageSearchDto imageSearchDto) {

    String date = imageSearchDto.getDate();
    String directory;

    if (StringUtils.isNotBlank(date) && date.matches("\\d{4}-\\d{2}-\\d{2}")) {
      directory = String.format("%s%s", imagesFolderPath, date);

      String clientId = imageSearchDto.getClientId();
      if (StringUtils.isNotBlank(clientId)) {
        directory = String.format("%s\\%s", directory, clientId);
      }
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
  public String getLastUpdate(String clientId) {
    String lastUpdateKey = StringUtils.isNotBlank(clientId) ? clientId : "no_client";
    return lastUpdates.get(lastUpdateKey);
  }
  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
