package com.motiondetection.configuration.spring;

import com.motiondetection.service.ServiceComponents;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

/**
 * App root spring configuration.
 */
@Configuration
@ComponentScan( basePackageClasses = { ServiceComponents.class })
public class AppRootConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Bean
    public PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {

        PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();

        Resource resource = applicationContext.getResource("classpath:config.properties");
        propertyPlaceholderConfigurer.setLocations(resource);

        return propertyPlaceholderConfigurer;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}