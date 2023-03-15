package com.example.myshoppingapp.model.pictures;

public class ImageDownloadModel {


  private final Long id;
  private final byte[] fileData;
  private final String contentType;

  private final String fileName;

  public ImageDownloadModel(Long id, byte[] fileData, String contentType, String fileName) {
    this.id = id;
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

  public Long getId() {
    return id;
  }
}
