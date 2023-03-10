package com.example.myshoppingapp.service;

import com.example.myshoppingapp.domain.AppUserDetails;
import com.example.myshoppingapp.domain.comments.Comment;
import com.example.myshoppingapp.domain.products.Product;
import com.example.myshoppingapp.domain.recipes.Recipe;
import com.example.myshoppingapp.domain.users.UserEntity;
import com.example.myshoppingapp.domain.users.UserInputDTO;
import com.example.myshoppingapp.domain.users.UserOutputDTO;
import com.example.myshoppingapp.repository.CommentRepository;
import com.example.myshoppingapp.repository.ProductRepository;
import com.example.myshoppingapp.repository.RecipeRepository;
import com.example.myshoppingapp.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Getter
@Setter
@Service
public class UserService {


    private UserRepository userRepository;
    private ProductRepository productRepository;
    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    private final AuthService authService;

    private final RecipeRepository recipeRepository;

    private final CommentRepository commentRepository;


    @Autowired
    public UserService(UserRepository userRepository, ProductRepository productRepository, ModelMapper modelMapper,
                       PasswordEncoder passwordEncoder, UserDetailsService userDetailsService,
                       AuthService authService, RecipeRepository recipeRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.authService = authService;
        this.recipeRepository = recipeRepository;
        this.commentRepository = commentRepository;
    }




    public UserEntity findByUsername(String username) {
        return this.userRepository.findUserEntityByUsername(username).orElseThrow(NoSuchElementException::new);
    }

    public UserEntity getLoggedUser(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.userRepository.findUserEntityByUsername(userDetails.getUsername())
                .orElseThrow(NoSuchElementException::new);

    }


    public Long getLoggedUserId() {
        return this.getLoggedUser().getId();
    }

    public UserOutputDTO getLoggedUserDTO() {
        return this.modelMapper.map(this.getLoggedUser(), UserOutputDTO.class);
    }

    @Transactional
    @Modifying
    public void updateUser(UserInputDTO userInputDTO) {
        UserEntity user = this.getLoggedUser();

        user.setUsername(userInputDTO.getUsername())
                .setEmail(userInputDTO.getEmail());

        if (!userInputDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userInputDTO.getPassword()));
        }

        this.userRepository.saveAndFlush(user);

    }

    @Transactional
    @Modifying
    public void deleteUserEntityById() {
        UserEntity userEntity = this.getLoggedUser();
        Optional<List<Product>> userProducts= this.productRepository.findAllByUserEntityId(userEntity.getId());

        if (userProducts.isPresent()) {
            for (Product userProduct : userProducts.get()) {
               this.productRepository.deleteById(userProduct.getId());
            }
        }

        List<Recipe> usersFavorites = userEntity.getFavoriteRecipes();
        if (usersFavorites.size() > 0) {
            usersFavorites.clear();
        }
        Optional<List<Recipe>> userRecipes = this.recipeRepository.findAllByAuthor(userEntity);

        if (userRecipes.isPresent()) {
            for (Recipe recipe : userRecipes.get()) {

                Optional<List<UserEntity>> userSavedThisRecipe =  userRepository.findAllByFavoriteRecipesContains(recipe);
                if (userSavedThisRecipe.isPresent()) {
                    for (UserEntity user : userSavedThisRecipe.get()) {
                        user.getFavoriteRecipes().remove(recipe);
                        this.userRepository.save(user);
                    }
                }
                this.recipeRepository.delete(recipe);
            }
        }

        Optional<List<Comment>> comments = this.commentRepository.findAllByAuthor(userEntity);
             comments.ifPresent(this.commentRepository::deleteAll);

        this.userRepository.delete(userEntity);

    }

    public void addBoughtProductToUser(Product product) {
        UserEntity userEntity = getLoggedUser();
        userEntity.getBoughtProducts().add(product);
        this.userRepository.saveAndFlush(userEntity);
    }






    public List<Recipe> getLoggedUserFavoriteList () {
       return this.getLoggedUser()
               .getFavoriteRecipes();
    }

    @Transactional
    @Modifying
    public void addRecipeToFavoriteList(Recipe recipe) {
        UserEntity userEntity = this.getLoggedUser();
        userEntity.getFavoriteRecipes().add(recipe);
        this.userRepository.saveAndFlush(userEntity);
    }
}


