package com.example.myshoppingapp.model.comments;

import com.example.myshoppingapp.model.BaseEntity;
import com.example.myshoppingapp.model.recipes.Recipe;
import com.example.myshoppingapp.model.users.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment extends BaseEntity {

    @Column(columnDefinition = "TEXT")
    private String text;

    @Column
    private boolean isApproved;
    @Column
    private LocalDateTime dateAdded;

    @Column
    private long rating;

   @ManyToOne (cascade = CascadeType.DETACH)
   @LazyCollection(LazyCollectionOption.FALSE)
    private UserEntity author;

    @ManyToOne(cascade = CascadeType.DETACH)
    private Recipe recipe;

    public Comment() {
        this.dateAdded = LocalDateTime.now();
        this.isApproved = false;
    }

    public void setApproved() {
        isApproved = true;
    }


    public Comment setText(String text) {
        this.text = text;
        return this;
    }

    public Comment setAuthor(UserEntity author) {
        this.author = author;
        return this;
    }

    public Comment setRecipe(Recipe recipe) {
        this.recipe = recipe;
        return this;
    }

    public Comment setRating(long rating) {
        this.rating = rating;
        return this;
    }
}
