package com.example.myshoppingapp.model.products;

import com.example.myshoppingapp.model.BaseEntity;
import com.example.myshoppingapp.model.recipes.Recipe;
import com.example.myshoppingapp.model.users.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @ManyToOne (fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn
    private UserEntity userEntity;

    @Column
    private long position;

    @ManyToMany (cascade = CascadeType.DETACH)
    private List<Recipe> recipeList;
    @Column
    private LocalDate boughtOn;

    @ManyToOne
    private UserEntity buyer;

    public Product() {
        this.recipeList = new ArrayList<>();
    }

    public Product(String name) {
        this();
        this.name = name;
    }

    public Product setBuyer(UserEntity buyer) {
        this.buyer = buyer;
        return this;
    }

    public Product setBoughtOn(LocalDate boughtOn) {
        this.boughtOn = boughtOn;
        return this;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public Product setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
        return this;
    }

    public Product setPosition(long position) {
        this.position = position;
        return this;
    }


}
