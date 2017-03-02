package motiondetection.serviceclient;

import motiondetection.model.MonitoringConfig;
import org.springframework.util.MultiValueMap;

/**
 * Data service - provides methods for uploading data to a specific destination.
 */
public interface DataService {

  /**
   * Connects to the specified URL and fetches the data it provides.
   * @param url - the URL of the remote service
   * @return the fetched configuration data as {@link MonitoringConfig}
   */
  MonitoringConfig getConfig(String url);

  /**
   * Pushes the data to the remote WS.
   * @param data - data to be pushed
   * @param url - remote WS URL
   */
  void pushData(MultiValueMap<String, Object> data, String url);
  
}
