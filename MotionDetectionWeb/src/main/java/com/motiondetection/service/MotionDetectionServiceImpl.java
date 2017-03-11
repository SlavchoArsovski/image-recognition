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
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
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

  private static final String NO_CLIENT = "no_client";
  private static final String CLIENT_LIST_SEPARATOR = ",";

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

    lastUpdates.put(NO_CLIENT, lastUpdate);
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

      String lastUpdateKey = StringUtils.isNotBlank(clientId) ? clientId : NO_CLIENT;
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

    StoredImagesDto storedImagesDto = new StoredImagesDto();

    String lastUpdate = getLastUpdateForClient(imageSearchDto.getClientId());
    storedImagesDto.setLastUpdate(lastUpdate);

    // search images for given client and date
    String imageDirectory = resolveImageDirectory(imageSearchDto);
    File storedImagesDir = new File(imageDirectory);
    FileFilter regexFileFilter = new RegexFileFilter(IMAGE_FILE_REGEX);
    File[] files = storedImagesDir.listFiles(regexFileFilter);

    if (files != null)  {

      int numberOfPages = calculateNumberOfPages(files);
      storedImagesDto.setNumberOfPages(numberOfPages);

      // filter files according to the filters (time of the day, page number)
      List<File> filesFiltered = filterFiles(files, imageSearchDto);

      filesFiltered
          .forEach(file -> {
            String imageAsEncodedString = getImageAsEncodedString(file);
            storedImagesDto.addImageEncoded(imageAsEncodedString);
          });
      }

    return storedImagesDto;
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

  private int calculateNumberOfPages(File[] files) {

    int numberOfPages = files.length / (int) pageSize;

    if (files.length % (int) pageSize != 0) {
      numberOfPages += 1;
    }
    return numberOfPages;
  }

  private List<File> filterFiles(File[] files, ImageSearchDto imageSearchDto) {

    Long pageNumber = calculatePageNumber(imageSearchDto);

    String date = String.format("%s-00-00-00", imageSearchDto.getDate());
    LocalDateTime dateTime = LocalDateTime.parse(date, fullTimeStampFormatter);

    String timeFrom = StringUtils.isNotBlank(imageSearchDto.getTimeFrom()) ? imageSearchDto.getTimeFrom() : "00";
    String timeTo = StringUtils.isNotBlank(imageSearchDto.getTimeTo()) ? imageSearchDto.getTimeTo() : "24";

    LocalDateTime dateTimeFrom = dateTime.plusHours(Long.valueOf(timeFrom));
    LocalDateTime dateTimeFromTo = dateTime.plusHours(Long.valueOf(timeTo));

    List<File> filesFiltered =
        Arrays
            .stream(files)
            .filter(fileInTimeRangePredicate(dateTimeFrom, dateTimeFromTo))
            .sorted(ImageFilesComparator.createImageFileComparatorInstance())
            .skip(pageNumber * pageSize)
            .limit(pageSize)
            .collect(Collectors.toList());

    return filesFiltered;
  }

  private Long calculatePageNumber(ImageSearchDto imageSearchDto) {

    Long pageNumber = imageSearchDto.getPageNumber();
    String clientId = imageSearchDto.getClientId();
    String clientLastUpdate = imageSearchDto.getLastUpdate();
    String lastUpdateOnServer = getLastUpdateForClient(clientId);

    boolean shouldResetPageNumber =
        pageNumber == null || pageNumber < 0 || !StringUtils.equals(clientLastUpdate, lastUpdateOnServer);

    if (shouldResetPageNumber) {
      pageNumber = 0L;
    }

    return pageNumber;
  }

  private Predicate<File> fileInTimeRangePredicate(LocalDateTime dateTimeFrom, LocalDateTime dateTimeFromTo) {
    return file -> {
      Matcher matcher = fileNamePattern.matcher(file.getName());

      if (matcher.matches()) {
        LocalDateTime imageDateTime = LocalDateTime.parse(matcher.group(1), fullTimeStampFormatter);

        boolean isImageInSelectedDateTimeRange =
            (imageDateTime.isAfter(dateTimeFrom) || imageDateTime.isEqual(dateTimeFrom))
                && imageDateTime.isBefore(dateTimeFromTo);

        if (isImageInSelectedDateTimeRange) {
          return true;
        }
      }
      return false;
    };
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
  public List<String> getClientList() {
    String[] clients = clientList.split(CLIENT_LIST_SEPARATOR);
    List<String> clintList = new ArrayList<>(Arrays.asList(clients));
    return clintList;
  }

  @Override
  public String getLastUpdateForClient(String clientId) {
    String key = StringUtils.isNotBlank(clientId) ? clientId : NO_CLIENT;
    return lastUpdates.get(key);
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
