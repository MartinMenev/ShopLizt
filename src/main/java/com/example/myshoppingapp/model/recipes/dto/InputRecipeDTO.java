package com.example.myshoppingapp.model.recipes.dto;

import com.example.myshoppingapp.model.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InputRecipeDTO implements Serializable {
    private long id;
   @NotNull
   @Size(min = 5, max = 100)
    private String name;

    private String url;

    private String description;
    @NotNull
    private Category category;

    private String imageUrl;


}
