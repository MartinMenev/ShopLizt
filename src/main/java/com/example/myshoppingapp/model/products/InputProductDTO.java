package com.example.myshoppingapp.model.products;

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
