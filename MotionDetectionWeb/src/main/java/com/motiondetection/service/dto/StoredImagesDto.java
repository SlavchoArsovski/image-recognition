package com.motiondetection.service.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Stored images DTO.
 */
public class StoredImagesDto {

  private List<String> imagesEncoded = new ArrayList<>();
  private int numberOfPages;
  private String lastUpdate;

  public List<String> getImagesEncoded() {
    return imagesEncoded;
  }

  public void setImagesEncoded(List<String> imagesEncoded) {
    this.imagesEncoded = imagesEncoded;
  }

  public int getNumberOfPages() {
    return numberOfPages;
  }

  public void setNumberOfPages(int numberOfPages) {
    this.numberOfPages = numberOfPages;
  }

  public String getLastUpdate() {
    return lastUpdate;
  }

  public void setLastUpdate(String lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public void addImageEncoded(String imageEncoded) {
    imagesEncoded.add(imageEncoded);
  }
}
