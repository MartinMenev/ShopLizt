package com.example.myshoppingapp.service;

import com.example.myshoppingapp.domain.beans.LoggedUser;
import com.example.myshoppingapp.domain.enums.UserRole;
import com.example.myshoppingapp.domain.users.LoginDTO;
import com.example.myshoppingapp.domain.users.RegisterUserDTO;
import com.example.myshoppingapp.domain.users.UserEntity;
import com.example.myshoppingapp.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class AuthService {
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private LoggedUser loggedUser;
    private final PasswordEncoder passwordEncoder;

    private UserDetailsService userDetailsService;

    @Autowired
    public AuthService(UserRepository userRepository, ModelMapper modelMapper, LoggedUser loggedUser, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.loggedUser = loggedUser;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Modifying
    public boolean register(RegisterUserDTO registerUserDTO) {
        UserEntity userEntity = this.modelMapper.map(registerUserDTO, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(registerUserDTO.getPassword()));
        if (this.userRepository.count() == 0) {
            userEntity.setUserRole(UserRole.ADMIN);
        } else {
            userEntity.setUserRole(UserRole.USER);
        }
        this.userRepository.save(userEntity);
        return true;
    }


    public void login(LoginDTO loginDTO) {
        UserEntity userEntity = userRepository.findUserEntityByUsername(loginDTO.getUsername()).get();
        this.loggedUser
                .setId(userEntity.getId())
                .setUsername(userEntity.getUsername())
                .setUserRole(userEntity.getUserRole());

    }

    public void logout() {
        this.loggedUser.clearFields();
    }

//
//    public void login(String userName) {
//        UserDetails userDetails =
//                userDetailsService.loadUserByUsername(userName);
//
//        Authentication auth =
//                new UsernamePasswordAuthenticationToken(
//                        userDetails,
//                        userDetails.getPassword(),
//                        userDetails.getAuthorities()
//                );
//
//        SecurityContextHolder.
//                getContext().
//                setAuthentication(auth);
//    }


}

