package motiondetection.serviceclient;

import motiondetection.model.MonitoringConfig;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Data Service Implementation.
 */
@Component
public class DataServiceImpl implements DataService {

  private Logger logger = Logger.getLogger(DataServiceImpl.class);

  @Autowired
  private RestTemplate restTemplate;

  /** {@inheritDoc}*/
  @Override
  public MonitoringConfig getConfig(String url) {

    MonitoringConfig config = restTemplate.getForObject(url, MonitoringConfig.class);
    logger.info(String.format("Fetched data: %s", config));
    System.out.println("Fetched data: " + config);
    return config;
  }

  /** {@inheritDoc}*/
  @Override
  public void pushData(MultiValueMap<String, Object> data, String url) {

    logger.info(String.format("Attempting to push the data by sending a request to the URL: %s", url));

    restTemplate.postForObject(
            url,
            new HttpEntity<MultiValueMap<String, Object>>(data),
            String.class);
  }
}
