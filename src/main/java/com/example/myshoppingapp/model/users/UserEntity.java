package com.example.myshoppingapp.model.users;

import com.example.myshoppingapp.model.BaseEntity;
import com.example.myshoppingapp.model.enums.UserRole;
import com.example.myshoppingapp.model.pictures.ImageEntity;
import com.example.myshoppingapp.model.pictures.Picture;
import com.example.myshoppingapp.model.products.Product;
import com.example.myshoppingapp.model.recipes.Recipe;
import com.example.myshoppingapp.model.roles.RoleEntity;
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


   @ManyToMany
   @LazyCollection(LazyCollectionOption.FALSE)
    private List<RoleEntity> roles;

    @OneToOne
    private Picture picture;

    @OneToOne (cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.FALSE)
    private ImageEntity imageEntity;

    @OneToMany (mappedBy = "buyer",cascade = CascadeType.REMOVE)
    private List<Product> boughtProducts;


    @ManyToMany (cascade=CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Recipe> favoriteRecipes;

    public UserEntity() {
        this.boughtProducts = new ArrayList<>();
        this.favoriteRecipes = new ArrayList<>();
        this.roles = new ArrayList<>();
    }

    public UserEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserEntity setImageEntity(ImageEntity imageEntity) {
        this.imageEntity = imageEntity;
        return this;
    }

    public UserEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserEntity addRole(RoleEntity role) {
        this.roles.add(role);
        return this;
    }

    public boolean isAdmin() {
        for (RoleEntity role : roles) {
            if (role.getRole().equals(UserRole.ADMIN)) {
                return true;
            }
        }
        return false;
    }

    public UserEntity setPicture(Picture picture) {
        this.picture = picture;
        return this;
    }
}
