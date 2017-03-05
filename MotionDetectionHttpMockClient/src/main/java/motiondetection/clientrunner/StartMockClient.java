package motiondetection.clientrunner;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import motiondetection.config.AppConfig;
/**
 * Starts the mock client.
 */
public class StartMockClient {

  private static final Logger logger =  Logger.getLogger(StartMockClient.class);

  public static void main(String []args) {

    logger.info("starting mock client");

    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);

    // add a shutdown hook for the above context...
    ctx.registerShutdownHook();
  }

}
