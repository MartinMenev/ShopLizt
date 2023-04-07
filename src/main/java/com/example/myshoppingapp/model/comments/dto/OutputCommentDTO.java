package com.example.myshoppingapp.model.comments.dto;

import com.example.myshoppingapp.model.recipes.Recipe;
import com.example.myshoppingapp.model.users.UserEntity;
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
    private boolean isApproved;
    private LocalDateTime dateAdded;
    private Long rating;
    private UserEntity author;
    private Recipe recipe;

    private LocalDate boughtOn;


    public String getDate(){
        return this.dateAdded.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }


    public String getRecipeName() {
        return this.recipe.getName();
    }
}
