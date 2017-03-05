package motiondetection.config;

import motiondetection.cronjob.CronJobExecutor;
import motiondetection.serviceclient.ClientComponents;
import motiondetection.util.ImageReader;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Spring configuration.
 */
@Configuration
@ComponentScan(basePackageClasses = { ClientComponents.class, CronJobExecutor.class, ImageReader.class })
@EnableScheduling
public class AppConfig implements ApplicationContextAware {

  private ApplicationContext appContext;

  @Bean
  public PropertyPlaceholderConfigurer propertyPlaceholderConfigurerMock() {

    PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();

    Resource resource = appContext.getResource("classpath:config-mock.properties");
    propertyPlaceholderConfigurer.setLocations(resource);

    return propertyPlaceholderConfigurer;
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

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.appContext = applicationContext;
  }
}
