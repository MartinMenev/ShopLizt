package com.example.myshoppingapp.model.users.dto;

import com.example.myshoppingapp.validation.loginMatch.ValidateLoginUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ValidateLoginUser
public class LoginDTO implements Serializable {
    @NotBlank
    @Size(min= 3, max = 20)
    private String username;
    @NotBlank
    @Size(min= 8)
    private String password;


}
