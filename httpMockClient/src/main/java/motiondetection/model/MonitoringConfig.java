package motiondetection.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Camera monitoring configuration DTO.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        defaultImpl = MonitoringConfig.class,
        visible = true,
        property = "_class"
)
public class MonitoringConfig {

  private float sensitivity;
  private float delayBetweenUploads;
  private int minimumMotionFrames;
  private float deltaThresh;
  private int resolutionWidth;
  private int resolutionHeight;

  // Default values
  public MonitoringConfig() {
    this.sensitivity = 0.5f;
    this.delayBetweenUploads = 2.5f;
    this.minimumMotionFrames = 4;
    this.deltaThresh = 3.5f;
    this.resolutionWidth = 768;
    this.resolutionHeight = 540;
  }

  @Override
  public String toString() {
    StringBuilder string = new StringBuilder();
    string.append("sensitivity: " + this.sensitivity + "\n");
    string.append("delayBetweenUploads: " + this.delayBetweenUploads + "\n");
    string.append("minimumMotionFrames: " + this.minimumMotionFrames + "\n");
    string.append("deltaThresh: " + this.deltaThresh + "\n");
    string.append("resolutionWidth: " + this.resolutionWidth + "\n");
    string.append("resolutionHeight: " + this.resolutionHeight + "\n");
    return string.toString();
  }

  public float getSensitivity() {
    return sensitivity;
  }

  public void setSensitivity(float sensitivity) {
    this.sensitivity = sensitivity;
  }

  public float getDelayBetweenUploads() {
    return delayBetweenUploads;
  }

  public void setDelayBetweenUploads(float delayBetweenUploads) {
    this.delayBetweenUploads = delayBetweenUploads;
  }

  public int getMinimumMotionFrames() {
    return minimumMotionFrames;
  }

  public void setMinimumMotionFrames(int minimumMotionFrames) {
    this.minimumMotionFrames = minimumMotionFrames;
  }

  public float getDeltaThresh() {
    return deltaThresh;
  }

  public void setDeltaThresh(float deltaThresh) {
    this.deltaThresh = deltaThresh;
  }

  public int getResolutionWidth() {
    return resolutionWidth;
  }

  public void setResolutionWidth(int resolutionWidth) {
    this.resolutionWidth = resolutionWidth;
  }

  public int getResolutionHeight() {
    return resolutionHeight;
  }

  public void setResolutionHeight(int resolutionHeight) {
    this.resolutionHeight = resolutionHeight;
  }
}
