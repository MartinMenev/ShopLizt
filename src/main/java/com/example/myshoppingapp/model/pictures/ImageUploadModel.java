package com.example.myshoppingapp.model.pictures;

import org.springframework.web.multipart.MultipartFile;

public class ImageUploadModel {

  private MultipartFile img;

  public MultipartFile getImg() {
    return img;
  }

  public ImageUploadModel setImg(MultipartFile img) {
    this.img = img;
    return this;
  }
}
