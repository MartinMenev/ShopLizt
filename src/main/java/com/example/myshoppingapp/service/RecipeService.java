package com.example.myshoppingapp.service;

import com.example.myshoppingapp.domain.enums.Category;
import com.example.myshoppingapp.domain.exceptions.ObjectNotFoundException;
import com.example.myshoppingapp.domain.pictures.Picture;
import com.example.myshoppingapp.domain.products.Product;
import com.example.myshoppingapp.domain.recipes.InputRecipeDTO;
import com.example.myshoppingapp.domain.recipes.OutputRecipeDTO;
import com.example.myshoppingapp.domain.recipes.Recipe;
import com.example.myshoppingapp.domain.users.UserEntity;
import com.example.myshoppingapp.repository.RecipeRepository;
import com.example.myshoppingapp.repository.UserRepository;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    private final UserRepository userRepository;

    private final UserService userService;
    private final ModelMapper modelMapper;

    private final ProductService productService;


    @Autowired
    public RecipeService(RecipeRepository recipeRepository, UserRepository userRepository, UserService userService,
                         ModelMapper modelMapper,
                         ProductService productService) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.productService = productService;
    }



    public void addRecipe(InputRecipeDTO inputRecipeDTO) {
        UserEntity authorId = this.userService.getLoggedUser();
        if (inputRecipeDTO.getImageUrl().isBlank()) {
            inputRecipeDTO.setImageUrl("https://images.pexels.com/photos/4033165/pexels-photo-4033165.jpeg?auto=compress&cs=tinysrgb&w=1600");
        }

        Recipe recipe = this.modelMapper.map(inputRecipeDTO, Recipe.class);
        recipe
                .setAuthor(authorId)
                .addPicture(new Picture(inputRecipeDTO.getImageUrl(), authorId));
        this.recipeRepository.saveAndFlush(recipe);


    }

    public Page<OutputRecipeDTO> showAllRecipes(Pageable pageable) {
        return recipeRepository.
                findAll(pageable).
                map(recipe -> this.modelMapper.map(recipe, OutputRecipeDTO.class));

    }



    @Transactional
    public List<OutputRecipeDTO> showLast5Recipes() {
        return this.recipeRepository
                .findAll()
                .stream()
                .filter(recipe -> !this.userService.getLoggedUser().getFavoriteRecipes().contains(recipe)
                        && !recipe.getAuthor().equals(this.userService.getLoggedUser()))
                .map(recipe -> modelMapper.map(recipe, OutputRecipeDTO.class))
                .sorted((a, b) -> b.getId().compareTo(a.getId()))
                .limit(5)
                .toList();
    }

    @Transactional
    public OutputRecipeDTO getRecipeById(Long id) {
        Optional<Recipe> recipe = this.recipeRepository
                .getRecipeById(id);
        return modelMapper.map(recipe, OutputRecipeDTO.class);
    }

    @Transactional
    @Modifying
    public void addRecipeRating(Long rating, long id) {
        if (rating != 0) {
            Recipe recipe = this.recipeRepository.getById(id);
            recipe.addRating(rating);
            this.recipeRepository.saveAndFlush(recipe);
        }

    }

    public List<OutputRecipeDTO> showRecipesByLoggedUser() {
        List<Recipe> myRecipes = this.recipeRepository
                .findAllByAuthorOrderByIdDesc(this.userService.getLoggedUser());

        List<Recipe> myFavorites = userService.getLoggedUserFavoriteList();
        if (myFavorites.size() != 0) {
            myRecipes.addAll(myFavorites);
        }
        return myRecipes.stream()
                .map(recipe -> modelMapper.map(recipe, OutputRecipeDTO.class))
                .toList();
    }





    public List<OutputRecipeDTO> getRecipesByCategory(String category) {
        return this.recipeRepository.findAllByCategory(Category.valueOf(category)).orElseThrow(null)
                .stream().map(recipe -> (modelMapper.map(recipe, OutputRecipeDTO.class)))
                .toList();
    }

    public List<OutputRecipeDTO> getRecipesByTextContent(String text) {
        return this.recipeRepository.findAllContainingSearchText(text).orElseThrow(null)
                .stream().map(recipe -> (modelMapper.map(recipe, OutputRecipeDTO.class)))
                .toList();

    }

    @Modifying
    @Transactional
    public void deleteById(long id) {
        Recipe recipe = this.recipeRepository.findById(id).get();

        Optional<List<UserEntity>> userSavedThisRecipe =  userRepository.findAllByFavoriteRecipesContains(recipe);
        if (userSavedThisRecipe.isPresent()) {
            for (UserEntity user : userSavedThisRecipe.get()) {
                user.getFavoriteRecipes().remove(recipe);
                this.userRepository.save(user);
            }
        }

        this.recipeRepository.delete(recipe);
    }

    @Modifying
    @Transactional
    public void updateRecipe(InputRecipeDTO inputRecipeDTO) {
        Recipe recipeToUpdate = this.recipeRepository.getRecipeById(inputRecipeDTO.getId()).get();
        recipeToUpdate
                .setName(inputRecipeDTO.getName())
                .setUrl(inputRecipeDTO.getUrl())
                .setDescription(inputRecipeDTO.getDescription())
                .setCategory(inputRecipeDTO.getCategory());

      if (!inputRecipeDTO.getImageUrl().isEmpty()) {
            Picture picture = new Picture(inputRecipeDTO.getImageUrl(), recipeToUpdate.getAuthor());
            recipeToUpdate.addPicture(picture);
       }

        this.recipeRepository.save(recipeToUpdate);

    }

    @Transactional
    @Modifying
    public void addProductToRecipe(Long id, String productName) {
        Product product = new Product(productName);
        this.productService.saveProduct(product);

        Recipe recipeToUpdate = this.recipeRepository.findById(id).get();
        recipeToUpdate.getProductList().add(product);
        this.recipeRepository.saveAndFlush(recipeToUpdate);
    }

    @Transactional
    @Modifying
    public void addProductToMyList(String name) {
        this.productService.addProductToMyList(name);
    }

    @Transactional
    @Modifying
    public void deleteProductFromRecipe(Long id, Long productId) {
        Product product = this.productService.findProductById(productId);
        Recipe recipe = this.recipeRepository.findById(id).get();
        recipe.getProductList().remove(product);
        this.recipeRepository.saveAndFlush(recipe);

    }

    @Transactional
    @Modifying
    public void addAllProductsToMyList(Long id) {
        List<Product> allRecipeProducts = this.recipeRepository
                .getRecipeById(id).get()
                .getProductList();

        for (Product product : allRecipeProducts) {
            this.productService.addProductToMyList(product.getName());
        }
    }

    @Transactional
    @Modifying
    public void saveRecipeToMyFavoriteList(Long id) {
        Recipe recipe = this.recipeRepository.findById(id).get();
        recipe.setNumberOfSaves(recipe.getNumberOfSaves() + 1);
        this.recipeRepository.saveAndFlush(recipe);
        this.userService.addRecipeToFavoriteList(recipe);


    }

    public List<OutputRecipeDTO> showLastAddedRecipes() {
        return this.recipeRepository
                .findAll()
                .stream()
                .map(recipe -> modelMapper.map(recipe, OutputRecipeDTO.class))
                .sorted((a, b) -> b.getId().compareTo(a.getId()))
                .limit(10)
                .toList();
    }

    @Transactional
    @Modifying
    public void removeRecipeFromMyCollection(long id) {
        Recipe recipeToRemove = this.recipeRepository.findById(id).get();
        UserEntity user = this.userService.getLoggedUser();
        if (user.getFavoriteRecipes().contains(recipeToRemove)) {
            user.getFavoriteRecipes().remove(recipeToRemove);
            userRepository.saveAndFlush(user);
            recipeToRemove.setNumberOfSaves(recipeToRemove.getNumberOfSaves() -1);
            this.recipeRepository.saveAndFlush(recipeToRemove);
            return;
        }
        this.recipeRepository.delete(recipeToRemove);
    }



}
