package com.example.myshoppingapp.validation.uniqueUsername;

import com.example.myshoppingapp.repository.UserRepository;
import com.example.myshoppingapp.validation.uniqueUsername.UniqueUsername;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {
    private final UserRepository userRepository;

    @Autowired
    public UniqueUsernameValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        return userRepository
                .findUserEntityByUsername(username)
                .isEmpty();
    }
}
