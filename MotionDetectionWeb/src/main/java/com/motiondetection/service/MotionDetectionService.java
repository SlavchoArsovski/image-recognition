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
   * Stores image to file system.
   * @param file the file to be stored.
   *
   * @return status.
   */
  UploadStatus storeImage(MultipartFile file, String clientId);

  /**
   * Retrieve stored images according to the given search.
   *
   * @param imageSearchDto contains search parameters.
   * @return {@link StoredImagesDto} containing the images as encoded strings that fulfill the search criteria.
   */
  StoredImagesDto getStoredImages(ImageSearchDto imageSearchDto);

  /**
   * Get timestamp of the last update for given client id.
   *
   * @param clientId the client id.
   * @return the last update timestamp as string.
   */
  String getLastUpdateForClient(String clientId);

  /**
   * @return list of client ids.
   */
  List<String> getClientList();

}
