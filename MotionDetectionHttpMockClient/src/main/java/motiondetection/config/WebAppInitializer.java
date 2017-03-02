package motiondetection.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Web App Initializer (Used instead of the standard xml deployment descriptor).
 */
@Configuration
public class WebAppInitializer implements WebApplicationInitializer {

  private Logger logger = LoggerFactory.getLogger(WebAppInitializer.class);
  
  public void onStartup(ServletContext servletContext) throws ServletException {

    // Create the 'root' Spring application context
    WebApplicationContext rootContext = getContext();
    servletContext.addListener(new ContextLoaderListener(rootContext));
    logger.info("Initializing the http client");

  }

  private AnnotationConfigWebApplicationContext getContext() {
    AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
    context.register(AppConfig.class);
    return context;
  }

}
