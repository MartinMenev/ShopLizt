package com.example.myshoppingapp.web;

import com.example.myshoppingapp.model.pictures.ImageUploadModel;
import com.example.myshoppingapp.model.recipes.InputRecipeDTO;
import com.example.myshoppingapp.service.ImageService;
import com.example.myshoppingapp.service.RecipeService;
import com.example.myshoppingapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class UploadController {
    private final ImageService imageService;
    private final UserService userService;

    private final RecipeService recipeService;

    @Autowired
    public UploadController(ImageService imageService, UserService userService, RecipeService recipeService) {
        this.imageService = imageService;
        this.userService = userService;
        this.recipeService = recipeService;
    }


    @GetMapping("/addImgToUser")
    public String displayUploadForm() {
        return "user/update-user";
    }

    @PostMapping("/addImgToUser")
    public String uploadImage(ImageUploadModel uploadModel) throws IOException {
        this.userService.addImageToUser(imageService.saveImage(uploadModel.getImg()));
        return "redirect:/update-user";
    }

    @GetMapping("/addImgToRecipe")
    public String displayUploadRecipeImage() {
        return "/recipe/my-recipe-collection";
    }



    @PostMapping("/addImgToRecipe/{id}")
    public String uploadRecipeImage(ImageUploadModel uploadModel,
                                    @PathVariable(value = "id") Long recipeId) throws IOException {
       this.recipeService.addImageToRecipe(recipeId, imageService.saveImage(uploadModel.getImg()));
        return "redirect:/my-collection";
    }

}
