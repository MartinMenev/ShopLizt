package com.example.myshoppingapp.service;

import com.example.myshoppingapp.model.pictures.ImageDownloadModel;
import com.example.myshoppingapp.model.pictures.ImageEntity;
import com.example.myshoppingapp.repository.ImageRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;

@Service
public class ImageService {

  private final ImageRepository imageRepository;

  public ImageService(ImageRepository imageRepository) {
    this.imageRepository = imageRepository;
  }

  public long saveImage(MultipartFile file) throws IOException {

    ImageEntity imageEntity = new ImageEntity().
        setFileName(file.getOriginalFilename()).
        setContentType(file.getContentType()).
        setData(file.getBytes());

    return imageRepository.save(imageEntity).getId();
  }

  public Optional<ImageDownloadModel> getFileById(long id) {
    var image = imageRepository.findById(id).orElseThrow();

    return Optional.of(new ImageDownloadModel(
        image.getData(),
        image.getContentType(),
        image.getFileName()
    ));
  }

  public Optional<ImageEntity> findById(long imageId) {
    return this.imageRepository.findById(imageId);
  }

  @Modifying
  @Transactional
  public void deleteById(ImageEntity imageEntity) {
    this.imageRepository.deleteById(imageEntity.getId());
  }
}
