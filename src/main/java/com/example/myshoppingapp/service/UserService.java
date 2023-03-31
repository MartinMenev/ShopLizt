package com.example.myshoppingapp.service;

import com.example.myshoppingapp.model.enums.UserRole;
import com.example.myshoppingapp.model.pictures.ImageEntity;
import com.example.myshoppingapp.model.products.Product;
import com.example.myshoppingapp.model.recipes.Recipe;
import com.example.myshoppingapp.model.roles.RoleEntity;
import com.example.myshoppingapp.model.users.UserEntity;
import com.example.myshoppingapp.model.users.UserInputDTO;
import com.example.myshoppingapp.model.users.UserOutputDTO;
import com.example.myshoppingapp.repository.ProductRepository;
import com.example.myshoppingapp.repository.RecipeRepository;
import com.example.myshoppingapp.repository.RoleRepository;
import com.example.myshoppingapp.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
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

    private final CommentService commentService;

    private final RoleRepository roleRepository;

    private final ImageService imageService;


    @Autowired
    public UserService(UserRepository userRepository, ProductRepository productRepository, ModelMapper modelMapper,
                       PasswordEncoder passwordEncoder, UserDetailsService userDetailsService,
                       AuthService authService, RecipeRepository recipeRepository,
                       CommentService commentService, RoleRepository roleRepository, ImageService imageService) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.authService = authService;
        this.recipeRepository = recipeRepository;
        this.commentService = commentService;
        this.roleRepository = roleRepository;
        this.imageService = imageService;
    }



    UserEntity getLoggedUser(String loggedName){
        return this.userRepository.findUserEntityByUsername(loggedName).orElseThrow(NoSuchElementException::new);

    }

    protected Long getLoggedUserId(String loggedName) {
        return this.getLoggedUser(loggedName).getId();
    }

    public UserOutputDTO getLoggedUserDTO(String loggedName) {
        return this.modelMapper.map(this.getLoggedUser(loggedName), UserOutputDTO.class);
    }

    @Transactional
    @Modifying
    public UserEntity updateUser(UserInputDTO userInputDTO, String loggedName) {
        UserEntity user = getLoggedUser(loggedName);

        user.setUsername(userInputDTO.getUsername())
                .setEmail(userInputDTO.getEmail());

        if (!userInputDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userInputDTO.getPassword()));
        }

        this.userRepository.saveAndFlush(user);
        return user;
    }

    @Transactional
    @Modifying
    public void deleteUserEntityById(String loggedName) {
        UserEntity userEntity = this.getLoggedUser(loggedName);
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
                this.commentService.deleteCommentsByRecipeId(recipe.getId());
                Optional<List<UserEntity>> usersSavedThisRecipe = userRepository.findAllByFavoriteRecipesContains(recipe);
                if (usersSavedThisRecipe.isPresent()) {
                    for (UserEntity user : usersSavedThisRecipe.get()) {
                        user.getFavoriteRecipes().remove(recipe);
                        this.userRepository.save(user);
                    }
                }
                this.recipeRepository.delete(recipe);
            }
        }


        this.userRepository.delete(userEntity);

    }




    public List<Recipe> getLoggedUserFavoriteList (String loggedName) {
       return this.getLoggedUser(loggedName)
               .getFavoriteRecipes();
    }

    @Transactional
    @Modifying
    public Recipe addRecipeToFavoriteList(Recipe recipe, String loggedName) {
        UserEntity userEntity = this.getLoggedUser(loggedName);
        userEntity.getFavoriteRecipes().add(recipe);
        this.userRepository.saveAndFlush(userEntity);
        return recipe;
    }

    public List<UserOutputDTO> searchUsersByName(String text) {
        return this.userRepository.findAllContainingSearchText(text).orElseThrow(null)
                .stream().map(u -> (modelMapper.map(u, UserOutputDTO.class)))
                .toList();


    }

    public List<UserOutputDTO> findAllUserNotAdmin() {
        return this.userRepository
                .findAll()
                .stream()
                .filter(user -> !user.isAdmin())
                .map(comment -> modelMapper.map(comment, UserOutputDTO.class))
                .sorted(Comparator.comparing(UserOutputDTO::getId))
                .toList();
    }

    public List<String> findAllAdminEmails() {
        return this.userRepository
                .findAll()
                .stream()
                .filter(UserEntity::isAdmin)
                .map(UserEntity::getEmail)
                .toList();
    }

    @Transactional
    @Modifying
    public void makeUserAdmin(Long id) {
        UserEntity userEntity = this.userRepository.getReferenceById(id);
        RoleEntity role = this.roleRepository.findRoleEntityByRole(UserRole.ADMIN).get();
        userEntity.addRole(role);
        this.userRepository.saveAndFlush(userEntity);
    }

    @Transactional
    @Modifying
    public ImageEntity addImageToUser(long imageId, String loggedName) {
        ImageEntity image = this.imageService.findById(imageId).orElseThrow();
        UserEntity userEntity = this.getLoggedUser(loggedName);
        if (userEntity.getImageEntity() != null) {
            this.getImageService().delete(userEntity.getImageEntity());
        }
        userEntity.setImageEntity(image);
        userRepository.saveAndFlush(userEntity);
        return image;
    }






}


