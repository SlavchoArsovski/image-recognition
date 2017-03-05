package motiondetection.cronjob;

import motiondetection.model.MonitoringConfig;
import motiondetection.serviceclient.DataService;
import motiondetection.util.ImageReader;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Used to execute cron jobs.
 */
@Service
public class CronJobExecutor {

  private Logger logger = Logger.getLogger(CronJobExecutor.class);
  
  @Autowired
  private DataService dataService;

  @Autowired
  private ImageReader imageReader;
  
  @Value("${remote.service.config.url}")
  private String configUrl;

  @Value("${remote.service.destination.url}")
  private String destinationURL;
  
  /**
   * Performs a specific task periodically, on a specific time interval.
   */
  @Scheduled(cron = "${cron.timer}")
  public void pushDataToRemoteWS() {

    logger.info(String.format("Performing task - get config %s", configUrl));
    MonitoringConfig config = dataService.getConfig(configUrl);

    MultiValueMap<String, Object> imageData = imageReader.readImage(
            getRandomImageFileName(),
            "images");

    if (imageData != null) {
      logger.info(String.format("Performing task - data push %s", destinationURL));
      dataService.pushData(imageData, destinationURL);
    } else {
      System.out.println("No image data found");
      logger.error("No image data found");
    }
  }

  private String getRandomImageFileName() {
    Random random = new Random();
    int next = random.nextInt(3);

    Map<Integer, String> imageMap = new HashMap<Integer, String>();
    imageMap.put(0, "red.jpg");
    imageMap.put(1, "blue.jpg");
    imageMap.put(2, "green.jpg");

    return imageMap.get(next);
  }
}