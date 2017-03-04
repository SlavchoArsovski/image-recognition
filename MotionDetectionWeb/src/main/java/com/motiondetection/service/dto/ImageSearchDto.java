package com.motiondetection.service.dto;

/**
 * Image search Model.
 */
public class ImageSearchDto {

  private String date;
  private String timeFrom;
  private String timeTo;
  private String clientId;

  public String getDate() {
    return date;
  }
  public void setDate(String date) {
    this.date = date;
  }
  public String getTimeFrom() {
    return timeFrom;
  }
  public void setTimeFrom(String timeFrom) {
    this.timeFrom = timeFrom;
  }
  public String getTimeTo() {
    return timeTo;
  }
  public void setTimeTo(String timeTo) {
    this.timeTo = timeTo;
  }
  public String getClientId() {
    return clientId;
  }
  public void setClientId(String clientId) {
    this.clientId = clientId;
  }
}
