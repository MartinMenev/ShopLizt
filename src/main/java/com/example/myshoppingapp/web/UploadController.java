package com.example.myshoppingapp.web;

import com.example.myshoppingapp.model.pictures.ImageUploadModel;
import com.example.myshoppingapp.service.ImageService;
import com.example.myshoppingapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
public class UploadController {
    private ImageService imageService;
    private UserService userService;

    @Autowired
    public UploadController(ImageService imageService, UserService userService) {
        this.imageService = imageService;
        this.userService = userService;
    }


    @GetMapping("/upload")
    public String displayUploadForm() {
        return "user/update-user";
    }

    @PostMapping("/upload")
    public String uploadImage(ImageUploadModel uploadModel) throws IOException {
        this.userService.addImageToUser(imageService.saveImage(uploadModel.getImg()));
        return "redirect:/update-user";
    }
}
