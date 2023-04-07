package com.example.myshoppingapp.model.comments.dto;

import com.example.myshoppingapp.model.recipes.Recipe;
import com.example.myshoppingapp.model.users.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class InputCommentDTO {

    private Long id;
    @NotNull
    @Size(min = 5, max = 250)
    private String text;
    private LocalDateTime dateAdded;

    private Long rating;
    private UserEntity author;
    private Recipe recipe;

    public InputCommentDTO() {
        this.dateAdded = LocalDateTime.now();
    }
}
