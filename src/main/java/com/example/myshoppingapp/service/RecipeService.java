package com.example.myshoppingapp.service;

import com.example.myshoppingapp.exceptions.ObjectNotFoundException;
import com.example.myshoppingapp.model.enums.Category;
import com.example.myshoppingapp.model.pictures.ImageEntity;
import com.example.myshoppingapp.model.products.Product;
import com.example.myshoppingapp.model.recipes.dto.InputRecipeDTO;
import com.example.myshoppingapp.model.recipes.dto.OutputRecipeDTO;
import com.example.myshoppingapp.model.recipes.Recipe;
import com.example.myshoppingapp.model.users.UserEntity;
import com.example.myshoppingapp.repository.RecipeRepository;
import com.example.myshoppingapp.repository.UserRepository;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.transaction.NotSupportedException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ProductService productService;
    private final ImageService imageService;
    private final CommentService commentService;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, UserRepository userRepository, UserService userService,
                         ModelMapper modelMapper,
                         ProductService productService, ImageService imageService, CommentService commentService) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.productService = productService;
        this.imageService = imageService;
        this.commentService = commentService;
    }

    public Long addRecipe(InputRecipeDTO inputRecipeDTO, String loggedName) {
        UserEntity authorId = this.userService.getLoggedUser(loggedName);
            inputRecipeDTO.setImageUrl("https://images.pexels.com/photos/4033165/pexels-photo-4033165.jpeg?auto=compress&cs=tinysrgb&w=1600");

        Recipe recipe = this.modelMapper.map(inputRecipeDTO, Recipe.class);
        recipe
                .setAuthor(authorId);
        this.recipeRepository.saveAndFlush(recipe);
        return recipe.getId();
    }

    public Page<OutputRecipeDTO> showAllRecipes(Pageable pageable) {
        return recipeRepository.
                findAll(pageable).
                map(recipe -> this.modelMapper.map(recipe, OutputRecipeDTO.class));

    }


    @Transactional
    public List<OutputRecipeDTO> showLast5Recipes(String loggedName) {
        return this.recipeRepository
                .findAll()
                .stream()
                .filter(recipe -> !this.userService.getLoggedUser(loggedName).getFavoriteRecipes().contains(recipe)
                        && !recipe.getAuthor().equals(this.userService.getLoggedUser(loggedName)))
                .map(recipe -> modelMapper.map(recipe, OutputRecipeDTO.class))
                .limit(5)
                .toList();
    }

    @Transactional
    public OutputRecipeDTO getRecipeById(Long id) {
        Optional<Recipe> recipe = this.recipeRepository
                .getRecipeById(id);
        if (recipe.isEmpty()) {
            throw new ObjectNotFoundException(id);
        }
        return modelMapper.map(recipe, OutputRecipeDTO.class);
    }


    @Transactional
    @Modifying
    public Long addRecipeRating(Long rating, long id) {
        if (rating != 0) {
            Recipe recipe = this.recipeRepository.getById(id);
            recipe.addRating(rating);
            this.recipeRepository.saveAndFlush(recipe);
        }
        return rating;
    }

    public List<OutputRecipeDTO> showRecipesByLoggedUser(String loggedName) {
        List<Recipe> myRecipes = this.recipeRepository
                .findAllByAuthorOrderByIdDesc(this.userService.getLoggedUser(loggedName));

        List<Recipe> myFavorites = userService.getLoggedUserFavoriteList(loggedName);
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
    public void deleteById(long id, String loggedName) throws NotSupportedException {
        UserEntity loggedUser = userService.getLoggedUser(loggedName);
        Recipe recipe = this.recipeRepository.findById(id).get();

        if (Objects.equals(loggedUser.getId(), recipe.getAuthor().getId()) || loggedUser.isAdmin()) {

            this.commentService.deleteCommentsByRecipeId(id);

            Optional<List<UserEntity>> userSavedThisRecipe =  userRepository.findAllByFavoriteRecipesContains(recipe);
            if (userSavedThisRecipe.isPresent()) {
                for (UserEntity user : userSavedThisRecipe.get()) {
                    user.getFavoriteRecipes().remove(recipe);
                    this.userRepository.save(user);
                }
            }
            this.recipeRepository.delete(recipe);

        } else {
            throw new NotSupportedException();
        }

    }

    @Modifying
    @Transactional
    public Recipe updateRecipe(InputRecipeDTO inputRecipeDTO) {
        Recipe recipeToUpdate = this.recipeRepository.getRecipeById(inputRecipeDTO.getId()).get();
        recipeToUpdate
                .setName(inputRecipeDTO.getName())
                .setUrl(inputRecipeDTO.getUrl())
                .setDescription(inputRecipeDTO.getDescription())
                .setCategory(inputRecipeDTO.getCategory());

        this.recipeRepository.save(recipeToUpdate);

      return recipeToUpdate;

    }

    @Transactional
    @Modifying
    public Product addProductToRecipe(Long id, String productName) {
        Product product = new Product(productName);
        this.productService.saveProduct(product);

        Recipe recipeToUpdate = this.recipeRepository.findById(id).get();
        recipeToUpdate.getProductList().add(product);
        this.recipeRepository.saveAndFlush(recipeToUpdate);
        return product;
    }

    @Transactional
    @Modifying
    public String addProductToMyList(String name, String loggedName) {
        this.productService.addProductToMyList(name, loggedName);
        return name;
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
    public void addAllProductsToMyList(Long id, String loggedName) {
        List<Product> allRecipeProducts = this.recipeRepository
                .getRecipeById(id).get()
                .getProductList();

        for (Product product : allRecipeProducts) {
            this.productService.addProductToMyList(product.getName(), loggedName);
        }
    }

    @Transactional
    @Modifying
    public void saveRecipeToMyFavoriteList(Long id, String loggedName) {
        Recipe recipe = this.recipeRepository.findById(id).get();
        recipe.setNumberOfSaves(recipe.getNumberOfSaves() + 1);
        this.recipeRepository.saveAndFlush(recipe);
        this.userService.addRecipeToFavoriteList(recipe, loggedName);


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
    public void removeRecipeFromMyCollection(long id, String loggedName) throws NotSupportedException {
        Recipe recipeToRemove = this.recipeRepository.findById(id).get();
        UserEntity user = this.userService.getLoggedUser(loggedName);
        if (user.getFavoriteRecipes().contains(recipeToRemove)) {
            user.getFavoriteRecipes().remove(recipeToRemove);
            userRepository.saveAndFlush(user);
            recipeToRemove.setNumberOfSaves(recipeToRemove.getNumberOfSaves() -1);
            this.recipeRepository.saveAndFlush(recipeToRemove);
            return;
        }
        this.deleteById(id, loggedName);
    }


    @Transactional
    @Modifying
    public ImageEntity addImageToRecipe(long recipeId, long imageId) {
        ImageEntity imageEntity = this.imageService.findById(imageId).orElseThrow();
        Recipe recipe = this.recipeRepository.findById(recipeId).orElseThrow();
        recipe.addImage(imageEntity);
        this.recipeRepository.saveAndFlush(recipe);
        return imageEntity;
    }

    public Optional<Recipe> getRecipeEntityById(Long recipeId) {
        return this.recipeRepository.getRecipeById(recipeId);
    }

    @Transactional
    @Modifying
    public void removeImageFromRecipe(long recipeId, ImageEntity image) {
        Recipe recipe = this.recipeRepository.getRecipeById(recipeId).orElseThrow();
        recipe.getImageList().remove(image);

        this.recipeRepository.saveAndFlush(recipe);
        this.imageService.delete(image);
    }
}
