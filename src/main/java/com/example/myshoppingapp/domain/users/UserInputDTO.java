package com.example.myshoppingapp.domain.users;

import com.example.myshoppingapp.domain.enums.UserRole;
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
        private UserRole userRole;
        private MultipartFile picture;
}
