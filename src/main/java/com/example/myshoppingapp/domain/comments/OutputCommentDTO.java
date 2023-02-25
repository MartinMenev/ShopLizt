package com.example.myshoppingapp.domain.comments;

import com.example.myshoppingapp.domain.pictures.Picture;
import com.example.myshoppingapp.domain.recipes.Recipe;
import com.example.myshoppingapp.domain.users.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OutputCommentDTO {

    private Long id;
    private String text;
    private LocalDateTime dateAdded;
    private Long rating;
    private UserEntity author;
    private Recipe recipe;

    private LocalDate boughtOn;
    public String getDate(){
        return this.dateAdded.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public String getAuthorPicUrl() {

        Picture picture = this.author.getPicture();
        if (picture == null) {
            return null;
        }

        return "/images/"+ picture.getPictureUrl();
    }

    public String getRecipeName() {
        return this.recipe.getName();
    }
}
