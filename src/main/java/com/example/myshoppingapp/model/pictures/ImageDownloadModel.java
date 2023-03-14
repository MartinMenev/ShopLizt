package com.example.myshoppingapp.model.pictures;

public class ImageDownloadModel {

  private final byte[] fileData;
  private final String contentType;

  private final String fileName;

  public ImageDownloadModel(byte[] fileData, String contentType, String fileName) {
    this.fileData = fileData;
    this.contentType = contentType;
    this.fileName = fileName;
  }

  public byte[] getFileData() {
    return fileData;
  }

  public String getContentType() {
    return contentType;
  }

  public String getFileName() {
    return fileName;
  }
}
