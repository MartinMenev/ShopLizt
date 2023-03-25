package com.example.myshoppingapp.web;

import com.example.myshoppingapp.model.comments.InputCommentDTO;
import com.example.myshoppingapp.model.recipes.OutputRecipeDTO;
import com.example.myshoppingapp.service.CommentService;
import com.example.myshoppingapp.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class CommentController {
    private final CommentService commentService;
    private final RecipeService recipeService;



    @Autowired
    public CommentController(CommentService commentService, RecipeService recipeService) {
        this.commentService = commentService;
        this.recipeService = recipeService;

    }

    @PostMapping("/save-comment/{id}")
    public String reviewRecipe(@PathVariable(value = "id") long id,
                               InputCommentDTO inputCommentDTO,
                               @RequestParam(name="rating") Long rating,
                               Principal user) {



        commentService.addComment(inputCommentDTO, id, user.getName());
        recipeService.addRecipeRating(rating, id);

        return "redirect:/recipe/{id}";
    }


    @PatchMapping("/approve-comment/{id}")
    public String approveComment(@PathVariable(value = "id") long id) {
        commentService.approveComment(id);

        return "redirect:/admin";
    }
    @ModelAttribute(name = "commentAddModel")
    public InputCommentDTO initInputCommentDTO() {
        return new InputCommentDTO();
    }


    @GetMapping("/comments/{id}")
    public String reviewRecipe(@PathVariable(value = "id") long id,
                               Model model) {
        OutputRecipeDTO outputRecipeDTO = this.recipeService.getRecipeById(id);
        model.addAttribute("comments", commentService.showAllComments(id));
        model.addAttribute("recipe", outputRecipeDTO);
        return "comment/all-comments";
    }

    @DeleteMapping ("/delete-comment/{id}")
    public String deleteComment(@PathVariable(value = "id") long id,
                                Principal user) {
        this.commentService.deleteComment(id, user.getName());



        return "redirect:/home";
    }

}
