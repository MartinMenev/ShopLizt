package com.example.myshoppingapp.web;

import com.example.myshoppingapp.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class UploadController {
    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/images";

    private final PictureService pictureService;

    @Autowired
    public UploadController(PictureService pictureService) {
        this.pictureService = pictureService;
    }


    @GetMapping("/upload")
    public String displayUploadForm() {
        return "user/update-user";
    }

    @PostMapping("/upload")
    public String uploadImage(Model model, @RequestParam("image") MultipartFile file) throws IOException {
        StringBuilder fileNames = new StringBuilder();
        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
        fileNames.append(file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());
        this.pictureService.addPicture(fileNames.toString());
        model.addAttribute("msg", "Uploaded images: " + fileNames.toString());
        return "redirect:/update-user";
    }
}
