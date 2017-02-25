package com.motiondetection.service;

import java.io.File;
import java.util.Comparator;

/**
 * Comparator used for sorting image files.
 */
public class ImageFilesComparator implements Comparator<File> {

  @Override
  public int compare(File f1, File f2) {
    return Long.valueOf(f2.lastModified()).compareTo(f1.lastModified());
  }
}
