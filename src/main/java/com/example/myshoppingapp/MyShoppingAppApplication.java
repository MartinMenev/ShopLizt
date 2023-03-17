package com.example.myshoppingapp;

import com.example.myshoppingapp.model.users.UserEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MyShoppingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyShoppingAppApplication.class, args);

    }

}
