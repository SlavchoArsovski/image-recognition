package motiondetection.util;

import motiondetection.cronjob.CronJobExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private Logger logger = LoggerFactory.getLogger(CronJobExecutor.class);

  public MultiValueMap<String, Object> readImage(String imageFileName, String imageFileLocation) {

    String imageResourcePath = String.format("%s/%s", imageFileLocation, imageFileName);
    logger.info("Reading image: ", imageResourcePath);

    Resource resource = new ClassPathResource(imageResourcePath);

    MultiValueMap<String, Object> imageFileParts = new LinkedMultiValueMap<String, Object>();
    imageFileParts.add("Content-Type", "image/jpeg");
    imageFileParts.add("image", resource);

    return imageFileParts;
  }
}