package com.example.myshoppingapp.model.users;

import com.example.myshoppingapp.model.pictures.ImageEntity;
import com.example.myshoppingapp.model.recipes.Recipe;
import com.example.myshoppingapp.model.roles.RoleEntity;
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
    private List<RoleEntity> roles;

    private ImageEntity imageEntity;

    private List<Recipe> favoriteRecipes;

    public String getRoles() {
        List<String> roleNames = this.roles.stream().map(r -> r.getRole().name()).toList();
        return String.format(String.join(" ,", roleNames));
    }


}
