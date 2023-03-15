package com.example.myshoppingapp.model.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInputDTO implements Serializable {

        private long id;
        private String username;
        private String password;
        private String email;

        private MultipartFile picture;
}