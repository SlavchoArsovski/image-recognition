package com.imagerecognition.service;

import org.springframework.web.multipart.MultipartFile;

import com.imagerecognition.enumaration.UploadStatus;
import com.imagerecognition.service.dto.StoredImagesDto;
/**
 * Image Recognition Service..
 */
public interface ImageRecognitionService {

  /**
   * Stores image to file system.
   * @param file the file to be stored.
   *
   * @return status.
   */
  UploadStatus storeImage(MultipartFile file);

  /**
   * Retrieve last ten stored images.
   *
   * @return {@link StoredImagesDto} containing the last 10 imaged as encoded strings.
   */
  StoredImagesDto getStoredImages();

}
