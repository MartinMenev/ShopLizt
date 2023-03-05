package com.example.myshoppingapp.web;

import com.example.myshoppingapp.domain.comments.InputCommentDTO;
import com.example.myshoppingapp.domain.recipes.InputRecipeDTO;
import com.example.myshoppingapp.domain.recipes.OutputRecipeDTO;
import com.example.myshoppingapp.service.CommentService;
import com.example.myshoppingapp.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

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
                               @RequestParam(name="rating") Long rating) {



        commentService.addComment(inputCommentDTO, id);
        recipeService.addRecipeRating(rating, id);

        return "redirect:/recipe/{id}";
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

}
