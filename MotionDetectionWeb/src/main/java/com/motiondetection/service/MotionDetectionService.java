package com.motiondetection.service;

import org.springframework.web.multipart.MultipartFile;

import com.motiondetection.enumeration.UploadStatus;
import com.motiondetection.service.dto.ImageSearchDto;
import com.motiondetection.service.dto.StoredImagesDto;
/**
 * Motion Detection Service.
 */
public interface MotionDetectionService {

  /**
   * Stores image to file system.
   * @param file the file to be stored.
   *
   * @return status.
   */
  UploadStatus storeImage(MultipartFile file);

  /**
   * Retrieve stored images according to the given search.
   *
   * @return {@link StoredImagesDto} containing the last 10 imaged as encoded strings.
   * @param imageSearchDto contains search parameters.
   */
  StoredImagesDto getStoredImages(ImageSearchDto imageSearchDto);

}
