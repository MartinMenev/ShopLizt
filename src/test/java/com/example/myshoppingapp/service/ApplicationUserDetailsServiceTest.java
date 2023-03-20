package com.example.myshoppingapp.service;

import com.example.myshoppingapp.model.enums.UserRole;
import com.example.myshoppingapp.model.roles.RoleEntity;
import com.example.myshoppingapp.model.users.UserEntity;
import com.example.myshoppingapp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.internal.util.Assert;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)

class ApplicationUserDetailsServiceTest {
    @Mock
    private UserRepository mockUserRepo;
    private ApplicationUserDetailsService toTest;

    @BeforeEach
    void setUp() {
        toTest = new ApplicationUserDetailsService(mockUserRepo);
    }

    @Test
    void loadUserByUsername_UserExists() {
        // arrange
        UserEntity testUserEntity =
                new UserEntity().
                        setEmail("pesho@example.com").
                        setPassword("topsecret").
                        setUsername("Pesho").
                        setRoles(List.of(
                                new RoleEntity().setRole(UserRole.ADMIN),
                                new RoleEntity().setRole(UserRole.USER)));

        when(mockUserRepo.findUserEntityByUsername(testUserEntity.getUsername())).
                thenReturn(Optional.of(testUserEntity));

        // act
        UserDetails userDetails = toTest.loadUserByUsername(testUserEntity.getUsername());

        // assert
        Assertions.assertEquals(testUserEntity.getUsername(),userDetails.getUsername());

        var authorities = userDetails.getAuthorities();
        Assertions.assertEquals(2, authorities.size());
    }

    @Test
    void testLoadUserByUsername_UserDoesNotExist() {

        // arrange
        // NOTE: No need to arrange anything, mocks return empty optionals.

        // act && assert
        Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> toTest.loadUserByUsername("non-existant@example.com")
        );
    }


}