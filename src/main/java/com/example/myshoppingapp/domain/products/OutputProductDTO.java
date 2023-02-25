package com.example.myshoppingapp.domain.products;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutputProductDTO implements Serializable{

        private Long id;
        private String name;

        private Long position;

    private LocalDate boughtOn;

    @Override
    public String toString() {
        return this.name;
    }
}

