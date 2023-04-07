package com.example.myshoppingapp.model.users.dto;

import com.example.myshoppingapp.validation.passwordMatch.FieldMatch;
import com.example.myshoppingapp.validation.uniqueUsername.UniqueUsername;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldMatch(first = "password", second = "confirmPassword")
public class RegisterUserDTO implements Serializable {
    @NotNull
    @UniqueUsername
    @Size(min = 5, max = 20)
    private String username;
    @NotNull
    @Email
    private String email;
    @NotNull
    @Size(min = 8, max = 20)
    private String password;

    @NotNull
    @Size(min = 8, max = 20)
    private String confirmPassword;


}
