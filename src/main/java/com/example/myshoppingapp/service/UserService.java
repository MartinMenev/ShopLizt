package com.example.myshoppingapp.service;

import com.example.myshoppingapp.domain.beans.LoggedUser;
import com.example.myshoppingapp.domain.products.Product;
import com.example.myshoppingapp.domain.recipes.Recipe;
import com.example.myshoppingapp.domain.users.*;
import com.example.myshoppingapp.repository.ProductRepository;
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
    @Autowired
    public UserService(UserRepository userRepository, ProductRepository productRepository, ModelMapper modelMapper,
                       PasswordEncoder passwordEncoder, UserDetailsService userDetailsService, AuthService authService) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.authService = authService;
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

    //delete user, but first delete all his product lists (if any)!
    public void deleteUserById(long id) {
        Optional<List<Product>> userProducts= this.productRepository.findAllByUserEntityId(id);
        if (userProducts.isPresent()) {
            for (Product userProduct : userProducts.get()) {
               this.productRepository.deleteById(userProduct.getId());
            }
        }
//        this.authService.logout();
        this.userRepository.deleteById(id);

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


