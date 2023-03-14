package com.example.myshoppingapp.service;

import com.example.myshoppingapp.model.pictures.OutputPictureDTO;
import com.example.myshoppingapp.model.pictures.Picture;
import com.example.myshoppingapp.model.users.UserEntity;
import com.example.myshoppingapp.repository.PictureRepository;
import com.example.myshoppingapp.repository.UserRepository;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Getter
@Service
public class PictureService {
    private final PictureRepository pictureRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public PictureService(PictureRepository pictureRepository, UserRepository userRepository,
                          UserService userService, ModelMapper modelMapper) {
        this.pictureRepository = pictureRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;

    }


    public void addPicture(String pictureUrl) {
        Picture picture = this.pictureRepository.findByAuthorId(userService.getLoggedUserId()).orElse(null);
        if (picture != null) {
            picture.setPictureUrl(pictureUrl);
            this.pictureRepository.saveAndFlush(picture);

        } else {
            picture = new Picture();
            picture.setAuthor(this.userService.getLoggedUser());
            picture.setPictureUrl(pictureUrl);
            this.pictureRepository.saveAndFlush(picture);
        }
        UserEntity userEntity = this.userService.getLoggedUser();
        userEntity.setPicture(picture);
        this.userRepository.saveAndFlush(userEntity);

    }

    public String getPictureUrlByLoggedUser(){
       Optional<Picture> picture = this.pictureRepository.findByAuthorId(this.userService.getLoggedUserId());
        String picUrl = picture.map(Picture::getPictureUrl).orElse(null);
        if (picUrl != null) {
            picUrl = "/images/"+picUrl;
        }
        return picUrl;
    }

    public String getPictureUrlByUserId(Long userId){
        Optional<Picture> picture = this.pictureRepository.findByAuthorId(userId);
        String picUrl = picture.map(Picture::getPictureUrl).orElse(null);
        if (picUrl != null) {
            picUrl = "/images/"+picUrl;
        }
        return picUrl;
    }

    public List<OutputPictureDTO> getAllPictures() {
        return this.pictureRepository.findAll()
                .stream()
                .map(picture -> modelMapper.map(picture, OutputPictureDTO.class))
                .toList();
    }
}
