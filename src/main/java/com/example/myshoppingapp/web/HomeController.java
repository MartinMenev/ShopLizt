package com.example.myshoppingapp.web;

import com.example.myshoppingapp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UserService userService;
    private final ProductService productService;
    private final RecipeService recipeService;
    private final CommentService commentService;

    @Autowired
    public HomeController(UserService userService, ProductService productService, RecipeService recipeService, CommentService commentService) {
        this.userService = userService;
        this.productService = productService;
        this.recipeService = recipeService;
        this.commentService = commentService;

    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("recipes", recipeService.showLastAddedRecipes());
        model.addAttribute("comments", commentService.showTopRatedComments());
        return "index";
    }



    @GetMapping("home")
    public String home(Model model) {
        model.addAttribute("username", userService.getLoggedUserDTO().getUsername());
        model.addAttribute("products", productService.getListedProducts());
        model.addAttribute("othersRecipes", recipeService.showLast5Recipes());
        model.addAttribute("recipes", recipeService.showLastAddedRecipes());
        model.addAttribute("myRecipes", recipeService.showRecipesByLoggedUser());
        return "home";
    }
}
