package com.example.myshoppingapp.domain.users;

import com.example.myshoppingapp.domain.BaseEntity;
import com.example.myshoppingapp.domain.enums.UserRole;
import com.example.myshoppingapp.domain.pictures.Picture;
import com.example.myshoppingapp.domain.products.Product;
import com.example.myshoppingapp.domain.recipes.Recipe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToOne
    private Picture picture;

    @OneToMany (mappedBy = "buyer",cascade = CascadeType.REMOVE)
    private List<Product> boughtProducts;

    @ManyToMany (cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Recipe> favoriteRecipes;

    public UserEntity() {
        this.boughtProducts = new ArrayList<>();
        this.favoriteRecipes = new ArrayList<>();
    }

    public UserEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserEntity setUserRole(UserRole userRole) {
        this.userRole = userRole;
        return this;
    }

    public UserEntity setPicture(Picture picture) {
        this.picture = picture;
        return this;
    }
}
