package com.motiondetection.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.motiondetection.enumeration.UploadStatus;
import com.motiondetection.service.dto.ImageSearchDto;
import com.motiondetection.service.dto.StoredImagesDto;
/**
 * Motion Detection Service.
 */
public interface MotionDetectionService {

  /**
   * @return list of client ids.
   */
  List<String> getClientList();

  /**
   * Stores image to file system.
   * @param file the file to be stored.
   *
   * @return status.
   */
  UploadStatus storeImage(MultipartFile file, String clientId);

  /**
   * Retrieve stored images according to the given search.
   *
   * @return {@link StoredImagesDto} containing the last 10 imaged as encoded strings.
   * @param imageSearchDto contains search parameters.
   */
  StoredImagesDto getStoredImages(ImageSearchDto imageSearchDto);

  String getLastUpdate(String clientId);

}
