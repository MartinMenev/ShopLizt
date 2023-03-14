package com.example.myshoppingapp.validation;

import com.example.myshoppingapp.model.users.LoginDTO;
import com.example.myshoppingapp.model.users.UserEntity;
import com.example.myshoppingapp.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class LoginUserValidator implements ConstraintValidator<ValidateLoginUser, LoginDTO> {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public LoginUserValidator(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void initialize(ValidateLoginUser constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LoginDTO loginDTO, ConstraintValidatorContext constraintValidatorContext) {
        Optional<UserEntity> loginCandidate = this.userRepository.findUserEntityByUsername(loginDTO.getUsername());
        return loginCandidate.isPresent()
                && passwordEncoder
                .matches(loginDTO.getPassword(),loginCandidate.get().getPassword());

    }
}
