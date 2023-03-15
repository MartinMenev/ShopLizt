package com.example.myshoppingapp.model.recipes;

import com.example.myshoppingapp.model.comments.Comment;
import com.example.myshoppingapp.model.enums.Category;
import com.example.myshoppingapp.model.pictures.Picture;
import com.example.myshoppingapp.model.products.Product;
import com.example.myshoppingapp.model.users.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

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

    public long getDays() {
        return DAYS.between(this.getDateAdded(), LocalDate.now()) ;
    }

}