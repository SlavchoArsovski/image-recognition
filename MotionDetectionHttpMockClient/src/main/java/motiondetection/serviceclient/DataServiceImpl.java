package motiondetection.serviceclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import motiondetection.model.MonitoringConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


/**
 * Data Service Implementation.
 */
@Component
public class DataServiceImpl implements DataService {

  private Logger logger = LoggerFactory.getLogger(DataServiceImpl.class);

  @Autowired
  private RestTemplate restTemplate;

  /** {@inheritDoc}*/
  @Override
  public MonitoringConfig getConfig(String url) {

    MonitoringConfig config = restTemplate.getForObject(url, MonitoringConfig.class);
    logger.info("Fetched data: {}", config);
    System.out.println("Fetched data: " + config);
    return config;
  }

  /** {@inheritDoc}*/
  @Override
  public void pushData(MultiValueMap<String, Object> data, String url) {

    logger.info("Attempting to push the data by sending a request to the URL: {}", url);

    restTemplate.postForObject(
            url,
            new HttpEntity<MultiValueMap<String, Object>>(data),
            String.class);
  }
}
