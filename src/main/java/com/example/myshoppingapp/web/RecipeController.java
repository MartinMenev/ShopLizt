package com.example.myshoppingapp.web;

import com.example.myshoppingapp.model.recipes.InputRecipeDTO;
import com.example.myshoppingapp.model.recipes.OutputRecipeDTO;
import com.example.myshoppingapp.service.CommentService;
import com.example.myshoppingapp.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.NotSupportedException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class RecipeController {
    private final RecipeService recipeService;
    private final CommentService commentService;




    @Autowired
    public RecipeController(RecipeService recipeService, CommentService commentService) {
        this.recipeService = recipeService;
        this.commentService = commentService;

    }

    @GetMapping("/add-recipe")
    public String addRecipe(){
        return "recipe/add-recipe";
    }

    @PostMapping("/add-recipe")
    public String doAddRecipe(@Valid @ModelAttribute(name = "recipeAddModel") InputRecipeDTO inputRecipeDTO,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              Principal user){

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("recipeAddModel", inputRecipeDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.recipeAddModel",
                            bindingResult);
            return "redirect:/add-recipe";
        }

             recipeService.addRecipe(inputRecipeDTO, user.getName());
        return "redirect:/my-collection";
    }





    @ModelAttribute(name = "recipeAddModel")
    public InputRecipeDTO initInputRecipeDTO() {
        return new InputRecipeDTO();
    }

    @GetMapping("/all-recipes")
    public String showRecipes(
        Model model,
        @PageableDefault(
                sort = "id",
                direction = Sort.Direction.DESC,
                size = 12) Pageable pageable) {
            model.addAttribute("recipes", recipeService.showAllRecipes(pageable));

            return "recipe/all-recipes";
    }

    @GetMapping("/save-recipe-to-favorites/{id}")
    public String saveRecipeToMyFavorites(@PathVariable(value = "id") Long id,
                                          Principal user) {
        this.recipeService.saveRecipeToMyFavoriteList(id, user.getName());
        return "redirect:/home";
    }

    @GetMapping("/recipe/{id}")
    public String reviewRecipe(@PathVariable(value = "id") Long id, Model model) {
        OutputRecipeDTO outputRecipeDTO = recipeService.getRecipeById(id);


        model.addAttribute("recipe", outputRecipeDTO);
        model.addAttribute("comments", commentService.showLatestComments(id));
        model.addAttribute("pictures", outputRecipeDTO.getImageList());
        return "recipe/recipe-details";
    }


    @GetMapping("/edit-recipe/{id}")
    public String editRecipe(@PathVariable(value = "id") Long id, Model model) {
        OutputRecipeDTO outputRecipeDTO = recipeService.getRecipeById(id);
        model.addAttribute("recipe", outputRecipeDTO);
        return "recipe/update-recipe";
    }

    @PostMapping("/add-product-to-recipe/{id}")
    public String addProductToRecipe(@PathVariable(value = "id") Long id,
                                     @RequestParam(value = "productName") String productName) {
        this.recipeService.addProductToRecipe(id, productName);
        return "redirect:/edit-recipe/{id}";
    }

    @GetMapping("/add-product-to-shopping-list/{id}/{name}")
    public String addProductToMyList(@PathVariable(value = "name") String name,
                                     @PathVariable(value = "id") Long id,
                                     Principal user) {
        this.recipeService.addProductToMyList(name, user.getName());
        return "redirect:/recipe/{id}";
    }

    @GetMapping("/add-all-products-to-shopping-list/{id}")
    public String addAllProductToMyList(@PathVariable(value = "id") Long id,
                                        Principal user) {
        this.recipeService.addAllProductsToMyList(id, user.getName());
        return "redirect:/product-list";
    }

    @GetMapping("/delete-product-from-recipe/{id}/{productId}")
    public String deleteProductFromRecipe(@PathVariable(value = "productId") Long productId,
                                          @PathVariable(value = "id") Long id) {
        this.recipeService.deleteProductFromRecipe(id, productId);
        return "redirect:/edit-recipe/{id}";
    }


    @PatchMapping("/update-recipe/{id}")
    public String doEditRecipe(@PathVariable(value = "id") Long id, Model model, InputRecipeDTO inputRecipeDTO) {
        recipeService.updateRecipe(inputRecipeDTO);
        return "redirect:/recipe/{id}";
    }

    @DeleteMapping("/delete-recipe/{id}")
    public String deleteById(@PathVariable(value = "id") long id,
                             Principal user) throws NotSupportedException {
        recipeService.deleteById(id, user.getName());
        return "redirect:/all-recipes";
    }

    @GetMapping("/filter-recipes")
    public String reviewRecipe(@RequestParam(value = "category") String category, Model model) {
        List<OutputRecipeDTO> recipeDTOList = this.recipeService.getRecipesByCategory(category);
        model.addAttribute("recipes", recipeDTOList);
        model.addAttribute("allRecipes", recipeService.showLastAddedRecipes());
        model.addAttribute("category", category);
        return "recipe/filter-recipe-by";
    }

    @GetMapping("/search-recipes")
    public String searchRecipes(@RequestParam(value = "text") String text, Model model) {
        List<OutputRecipeDTO> recipeDTOList = this.recipeService.getRecipesByTextContent(text);
        model.addAttribute("recipes", recipeDTOList);
        model.addAttribute("allRecipes", recipeService.showLastAddedRecipes());
        model.addAttribute("text", text);
        return "recipe/search-recipes";
    }



    @GetMapping("/my-collection")
    public String myCollection(Model model, Principal user) {
        model.addAttribute("recipes", recipeService.showRecipesByLoggedUser(user.getName()));
        model.addAttribute("allRecipes", recipeService.showLastAddedRecipes());
        return "recipe/my-recipe-collection";
    }

    @GetMapping("/remove-from-favorites/{id}")
    public String removeFromMyCollection(@PathVariable(value = "id") long id,
                                         Principal user) throws NotSupportedException {
        recipeService.removeRecipeFromMyCollection(id, user.getName());
        return "redirect:/my-collection";
    }


}
