package com.example.myshoppingapp.domain.products;

import com.example.myshoppingapp.domain.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InputProductDTO implements Serializable {
    private Long id;

    private String name;

}
