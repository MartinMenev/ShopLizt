package com.example.myshoppingapp.domain.comments;

import com.example.myshoppingapp.domain.recipes.Recipe;
import com.example.myshoppingapp.domain.users.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class InputCommentDTO {
    private Long id;
    private String text;
    private LocalDateTime dateAdded;
    private long rating;
    private UserEntity author;
    private Recipe recipe;

    public InputCommentDTO() {
        this.dateAdded = LocalDateTime.now();
    }
}
