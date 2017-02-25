package com.imagerecognition.service.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Stored images DTO.
 */
public class StoredImagesDto {

  private List<String> imagesEncoded = new ArrayList<>();

  public List<String> getImagesEncoded() {
    return imagesEncoded;
  }

  public void setImagesEncoded(List<String> imagesEncoded) {
    this.imagesEncoded = imagesEncoded;
  }

  public void addImageEncoded(String imageEncoded) {
    imagesEncoded.add(imageEncoded);
  }
}
