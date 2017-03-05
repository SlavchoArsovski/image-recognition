package motiondetection.util;

import motiondetection.cronjob.CronJobExecutor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Image helper.
 */
@Component
public class ImageReader {

  private Logger logger = Logger.getLogger(CronJobExecutor.class);

  @Value("${clientId}")
  private String clientId;

  public MultiValueMap<String, Object> readImage(String imageFileName, String imageFileLocation) {

    String imageResourcePath = String.format("%s/%s", imageFileLocation, imageFileName);
    logger.info("Reading image: " + imageResourcePath);

    Resource resource = new ClassPathResource(imageResourcePath);

    MultiValueMap<String, Object> imageFileParts = new LinkedMultiValueMap<String, Object>();
    imageFileParts.add("Content-Type", "image/jpeg");
    imageFileParts.add("image", resource);
    imageFileParts.add("clientId", clientId);

    return imageFileParts;
  }
}