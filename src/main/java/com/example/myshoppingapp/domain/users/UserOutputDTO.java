package com.example.myshoppingapp.domain.users;

import com.example.myshoppingapp.domain.enums.UserRole;
import com.example.myshoppingapp.domain.pictures.Picture;
import com.example.myshoppingapp.domain.recipes.Recipe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class UserOutputDTO implements Serializable {
    private long id;
    private String username;
    private String password;
    private String email;
    private UserRole userRole;
    private Picture picture;

    private List<Recipe> favoriteRecipes;


}
