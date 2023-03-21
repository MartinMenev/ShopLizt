package com.example.myshoppingapp.service;

import com.example.myshoppingapp.model.users.UserEntity;
import com.example.myshoppingapp.model.users.UserInputDTO;
import com.example.myshoppingapp.repository.ProductRepository;
import com.example.myshoppingapp.repository.RecipeRepository;
import com.example.myshoppingapp.repository.RoleRepository;
import com.example.myshoppingapp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private ProductRepository mockProductRepository;
    @Mock
    private ModelMapper mockModelMapper;
    @Mock
    private PasswordEncoder mockPasswordEncoder;
    @Mock
    private UserDetailsService mockUserDetailsService;
    @Mock
    private AuthService mockAuthService;
    @Mock
    private RecipeRepository mockRecipeRepository;
    @Mock
    private CommentService mockCommentService;
    @Mock
    private RoleRepository mockRoleRepository;
    @Mock
    private ImageService mockImageService;

    private UserService toTest;

    @BeforeEach
    void setUp() {
        toTest = new UserService(
                mockUserRepository, mockProductRepository, mockModelMapper, mockPasswordEncoder,
                mockUserDetailsService, mockAuthService, mockRecipeRepository, mockCommentService,
                mockRoleRepository, mockImageService);

    }

    @Test
    public void testFindByUsername(){
        UserEntity testUser = (UserEntity) new UserEntity().setUsername("martin");
        when(mockUserRepository.findUserEntityByUsername("martin")).
                thenReturn(Optional.of(testUser));
        Assertions.assertEquals(testUser, toTest.findByUsername("martin"));

        }



}