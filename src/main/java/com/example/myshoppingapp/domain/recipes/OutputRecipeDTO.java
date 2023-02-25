package com.example.myshoppingapp.domain.recipes;

import com.example.myshoppingapp.domain.comments.Comment;
import com.example.myshoppingapp.domain.enums.Category;
import com.example.myshoppingapp.domain.pictures.Picture;
import com.example.myshoppingapp.domain.products.Product;
import com.example.myshoppingapp.domain.users.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutputRecipeDTO {
    private Long id;
    private String name;
    private String url;
    private String description;
    private Category category;
    private double rating;
    private List<Picture> pictureList;

    private String imageUrl;

    private long numberOfSaves;
    private UserEntity author;
    private List<Comment> commentList;

    private List<Product> productList;

    private LocalDate dateAdded;

    public String getAuthorName(){
        return this.author.getUsername();
    }

    public boolean hasImageUrl(){
        return this.imageUrl != null;
    }
}
