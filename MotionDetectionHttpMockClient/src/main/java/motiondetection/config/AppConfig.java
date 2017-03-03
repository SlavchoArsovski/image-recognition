package motiondetection.config;

import java.util.ArrayList;
import java.util.List;

import motiondetection.cronjob.CronJobExecutor;
import motiondetection.serviceclient.ClientComponents;
import motiondetection.util.ImageReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Spring configuration.
 */
@Configuration
@ComponentScan(
    basePackageClasses = {ClientComponents.class, CronJobExecutor.class, ImageReader.class})
@EnableScheduling
public class AppConfig {
  
  /**
   * PropertySourcesPlaceholderConfigurer instance.
   * 
   * @return the propertyConfigurer
   */
  @Bean
  public PropertySourcesPlaceholderConfigurer propertyConfigIn() {
    return new PropertySourcesPlaceholderConfigurer();
  }

  @Bean
  public RestTemplate restTemplate() {

    RestTemplate restTemplate = new RestTemplate();

    MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
    ObjectMapper objectMapper = new ObjectMapper();
    jsonMessageConverter.setObjectMapper(objectMapper);

    restTemplate.getMessageConverters().add(jsonMessageConverter);

    return restTemplate;
  }
  
}
