package com.example.myshoppingapp.service;

import com.example.myshoppingapp.model.enums.UserRole;
import com.example.myshoppingapp.model.users.dto.RegisterUserDTO;
import com.example.myshoppingapp.model.users.UserEntity;
import com.example.myshoppingapp.repository.RoleRepository;
import com.example.myshoppingapp.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Getter
@Setter
@Service
public class AuthService {

    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private UserDetailsService userDetailsService;
    private final EmailService emailService;

    @Autowired
    public AuthService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder,
                       RoleRepository roleRepository, UserDetailsService userDetailsService, EmailService emailService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userDetailsService = userDetailsService;
        this.emailService = emailService;
    }

    @Modifying
    @Transactional
    public void register(RegisterUserDTO registerUserDTO) {
        UserEntity userEntity = this.modelMapper.map(registerUserDTO, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));
        userEntity.addRole(this.roleRepository.findRoleEntityByRole(UserRole.USER).orElseThrow());
        if (this.userRepository.count() == 0) {
            userEntity.addRole(this.roleRepository.findRoleEntityByRole(UserRole.ADMIN).orElseThrow());
        }
        userRepository.save(userEntity);

            this.emailService.sendRegistrationEmail(registerUserDTO.getEmail(), registerUserDTO.getUsername());

    }

}

