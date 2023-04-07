package com.example.myshoppingapp.service;

import com.example.myshoppingapp.model.enums.UserRole;
import com.example.myshoppingapp.model.pictures.ImageEntity;
import com.example.myshoppingapp.model.recipes.Recipe;
import com.example.myshoppingapp.model.roles.RoleEntity;
import com.example.myshoppingapp.model.users.UserEntity;
import com.example.myshoppingapp.model.users.dto.UserInputDTO;
import com.example.myshoppingapp.repository.ProductRepository;
import com.example.myshoppingapp.repository.RecipeRepository;
import com.example.myshoppingapp.repository.RoleRepository;
import com.example.myshoppingapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
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
        testUser.getFavoriteRecipes().add(new Recipe());

        testInputUserDTO = new UserInputDTO();


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

    @Test
    public void testGetLoggedUserId(){
        when(mockUserRepository.findUserEntityByUsername(testUser.getUsername())).
                thenReturn(Optional.of(testUser));

        assertEquals(testUser.getId(), toTest.getLoggedUserId(testUser.getUsername()));
    }

    @Test
    public void testDeleteUserById() {

    }

    @Test
    public void testGetFavoriteListOfLoggedUser() {
        List<Recipe> favoriteRecipeList = testUser.getFavoriteRecipes();

        when(mockUserRepository.findUserEntityByUsername(testUser.getUsername())).
                thenReturn(Optional.of(testUser));

        assertEquals(favoriteRecipeList, toTest.getLoggedUserFavoriteList(testUser.getUsername()));

    }

    @Test
    public void testAddingRecipeToFavoriteList() {
        Recipe testRecipe = new Recipe();
        String name = "martin";
        int numberOfRecipes = testUser.getFavoriteRecipes().size();

        when(mockUserRepository.findUserEntityByUsername(name)).
                thenReturn(Optional.of(testUser));

        testUser.getFavoriteRecipes().add(testRecipe);

        Recipe newAddedRecipe = toTest.addRecipeToFavoriteList(testRecipe, name);

        assertEquals(testRecipe, newAddedRecipe);
    }

    @Test
    public void testToFindAllNonAdminUsers() {
        testUser.setRoles(List.of(new RoleEntity().setRole(UserRole.ADMIN)));

        when (mockUserRepository.findAll()).thenReturn(List.of(testUser));
        assertEquals(0, toTest.findAllUserNotAdmin().size());

    }

    @Test
    public void testToFindAllAdminEmails() {
        testUser.setRoles(List.of(new RoleEntity().setRole(UserRole.ADMIN)));

        when (mockUserRepository.findAll()).thenReturn(List.of(testUser));

        assertEquals(1, toTest.findAllAdminEmails().size());

    }

    @Test
    public void testToMakeUserAdmin() {
        int initialNumberOfRoles = testUser.getRoles().size();
        when(mockUserRepository.getReferenceById(testUser.getId())).thenReturn(testUser);
        when(mockRoleRepository.findRoleEntityByRole(UserRole.ADMIN)).thenReturn(Optional.of(new RoleEntity()));

        toTest.makeUserAdmin(testUser.getId());
        assertEquals(initialNumberOfRoles + 1, testUser.getRoles().size());

    }

    @Test
    public void testAddingImageToUser() {
        ImageEntity image = new ImageEntity();
        String name = testUser.getUsername();

        when(mockUserRepository.findUserEntityByUsername(name)).thenReturn(Optional.of(testUser));
        when(mockImageService.findById(image.getId())).thenReturn(Optional.of(image));

        assertEquals(image, toTest.addImageToUser(image.getId(), name));

    }



}