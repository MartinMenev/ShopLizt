package com.example.myshoppingapp.service;

import com.example.myshoppingapp.model.users.UserEntity;
import com.example.myshoppingapp.model.users.UserInputDTO;
import com.example.myshoppingapp.repository.ProductRepository;
import com.example.myshoppingapp.repository.RecipeRepository;
import com.example.myshoppingapp.repository.RoleRepository;
import com.example.myshoppingapp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private ProductRepository mockProductRepository;
   @Mock
    private ModelMapper modelMapper;
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
    private UserEntity testUser;
    private UserInputDTO testInputUserDTO;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        toTest = new UserService(
                mockUserRepository, mockProductRepository, modelMapper, mockPasswordEncoder,
                mockUserDetailsService, mockAuthService, mockRecipeRepository, mockCommentService,
                mockRoleRepository, mockImageService);

        modelMapper = new ModelMapper();

        testUser = new UserEntity()
                .setUsername("martin")
                .setEmail("martin@abv.bg");
        testUser.setId(3L);

        testInputUserDTO = new UserInputDTO();


    }

    @Test
    public void testFindByUsername(){
        UserEntity testUser = new UserEntity().setUsername("martin");
        when(mockUserRepository.findUserEntityByUsername("martin")).
                thenReturn(Optional.of(testUser));
        Assertions.assertEquals(testUser, toTest.findByUsername("martin"));
        }


    @Test

    public void testUpdateUserSuccessfully(){
        testInputUserDTO.setUsername("martin123");
        testInputUserDTO.setEmail("martin@gmail.com");
        testInputUserDTO.setPassword("newPassword");
        testInputUserDTO.setId(testUser.getId());
        String name = testUser.getUsername();


        when(mockUserRepository.findUserEntityByUsername(name))
                .thenReturn(Optional.of(testUser));
        UserEntity updatedUser = toTest.updateUser(testInputUserDTO, name);

        assertEquals(testInputUserDTO.getUsername(), updatedUser.getUsername());
        assertEquals(testInputUserDTO.getId(), updatedUser.getId());
        assertEquals(testInputUserDTO.getEmail(), updatedUser.getEmail());
        verify(mockUserRepository).saveAndFlush(testUser);
    }

}