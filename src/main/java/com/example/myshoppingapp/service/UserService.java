package com.example.myshoppingapp.service;

import com.example.myshoppingapp.domain.comments.Comment;
import com.example.myshoppingapp.domain.enums.UserRole;
import com.example.myshoppingapp.domain.products.Product;
import com.example.myshoppingapp.domain.recipes.Recipe;
import com.example.myshoppingapp.domain.roles.RoleEntity;
import com.example.myshoppingapp.domain.users.UserEntity;
import com.example.myshoppingapp.domain.users.UserInputDTO;
import com.example.myshoppingapp.domain.users.UserOutputDTO;
import com.example.myshoppingapp.repository.*;
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

    private final CommentRepository commentRepository;

    private final RoleRepository roleRepository;


    @Autowired
    public UserService(UserRepository userRepository, ProductRepository productRepository, ModelMapper modelMapper,
                       PasswordEncoder passwordEncoder, UserDetailsService userDetailsService,
                       AuthService authService, RecipeRepository recipeRepository, CommentRepository commentRepository,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.authService = authService;
        this.recipeRepository = recipeRepository;
        this.commentRepository = commentRepository;
        this.roleRepository = roleRepository;
    }




    UserEntity findByUsername(String username) {
        return this.userRepository.findUserEntityByUsername(username).orElseThrow(NoSuchElementException::new);
    }

    UserEntity getLoggedUser(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.userRepository.findUserEntityByUsername(userDetails.getUsername())
                .orElseThrow(NoSuchElementException::new);

    }


    protected Long getLoggedUserId() {
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
}


