package com.example.myshoppingapp.model.pictures;


import javax.persistence.*;

@Entity
@Table(name = "images")
public class ImageEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String fileName;

  private String contentType;



  @Lob
  @Column(length = Integer.MAX_VALUE)
  private byte[] data;

  public long getId() {
    return id;
  }

  public ImageEntity setId(long id) {
    this.id = id;
    return this;
  }

  public String getFileName() {
    return fileName;
  }


  public String getContentType() {
    return contentType;
  }


  public byte[] getData() {
    return data;
  }

  public ImageEntity setFileName(String fileName) {
    this.fileName = fileName;
    return this;
  }

  public ImageEntity setContentType(String contentType) {
    this.contentType = contentType;
    return this;
  }

  public ImageEntity setData(byte[] data) {
    this.data = data;
    return this;
  }


}
