package com.example.myshoppingapp.service;

import com.example.myshoppingapp.model.pictures.dto.ImageDownloadModel;
import com.example.myshoppingapp.model.pictures.ImageEntity;
import com.example.myshoppingapp.model.recipes.dto.OutputRecipeDTO;
import com.example.myshoppingapp.model.users.UserEntity;
import com.example.myshoppingapp.repository.ImageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
public class ImageService {

  private final ImageRepository imageRepository;
  private final UserService userService;
  private final RecipeService recipeService;
  private final ModelMapper modelMapper;

  @Lazy
  public ImageService(ImageRepository imageRepository, UserService userService, RecipeService recipeService, ModelMapper modelMapper) {
    this.imageRepository = imageRepository;
    this.userService = userService;
    this.recipeService = recipeService;
    this.modelMapper = modelMapper;
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
            image.getId(),
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
  public void deleteImage(long id, String name, long recipeId ) {
    UserEntity loggedUser = this.userService.getLoggedUser(name);
    OutputRecipeDTO recipe = this.recipeService.getRecipeById(recipeId);
    ImageEntity image = this.imageRepository.getReferenceById(id);

    if (Objects.equals(loggedUser.getId(), recipe.getAuthor().getId()) || loggedUser.isAdmin()) {
      this.recipeService.removeImageFromRecipe(recipeId, image);

    }
  }


  public void delete(ImageEntity imageEntity) {
    this.imageRepository.delete(imageEntity);
  }

}
