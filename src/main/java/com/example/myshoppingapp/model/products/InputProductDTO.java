package com.example.myshoppingapp.model.products;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InputProductDTO extends Product implements Serializable {

    private String name;


}
