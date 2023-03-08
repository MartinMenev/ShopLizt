package com.example.myshoppingapp.service;

import com.example.myshoppingapp.domain.enums.UserRole;
import com.example.myshoppingapp.domain.roles.RoleEntity;
import com.example.myshoppingapp.domain.users.RegisterUserDTO;
import com.example.myshoppingapp.domain.users.UserEntity;
import com.example.myshoppingapp.repository.RoleRepository;
import com.example.myshoppingapp.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class AuthService {
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private UserDetailsService userDetailsService;

    @Autowired
    public AuthService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userDetailsService = userDetailsService;
    }

    @Modifying
    public void register(RegisterUserDTO registerUserDTO) {
        UserEntity userEntity = this.modelMapper.map(registerUserDTO, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));
        if (this.userRepository.count() == 0) {
            userEntity.addRole(this.roleRepository.findRoleEntityByRole(UserRole.ADMIN).orElseThrow());
        } else {
            userEntity.addRole(this.roleRepository.findRoleEntityByRole(UserRole.USER).orElseThrow());
        }
        userRepository.save(userEntity);


    }



}

