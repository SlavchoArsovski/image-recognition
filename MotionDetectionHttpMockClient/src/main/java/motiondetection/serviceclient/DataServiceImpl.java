package motiondetection.serviceclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import motiondetection.model.MonitoringConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  /** {@inheritDoc}*/
  @Override
  public MonitoringConfig getConfig(String url) {
    RestTemplate restTemplate = new RestTemplate();

    List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
    MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
    ObjectMapper objectMapper = new ObjectMapper();
    jsonMessageConverter.setObjectMapper(objectMapper);
    messageConverters.add(jsonMessageConverter);
    restTemplate.setMessageConverters(messageConverters);

    MonitoringConfig config = restTemplate.getForObject(url, MonitoringConfig.class);
    logger.info("Fetched data: {}", config);
    System.out.println("Fetched data: " + config);
    return config;
  }

  /** {@inheritDoc}*/
  @Override
  public void pushData(MultiValueMap<String, Object> data, String url) {

    logger.info("Attempting to push the data by sending a request to the URL: {}", url);
    RestTemplate restTemplate = new RestTemplate();

    restTemplate.postForObject(
            url,
            new HttpEntity<MultiValueMap<String, Object>>(data),
            String.class);
  }
}
