package motiondetection.config;

import motiondetection.cronjob.CronJobExecutor;
import motiondetection.serviceclient.ClientComponents;
import motiondetection.util.ImageReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;

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
  
}
