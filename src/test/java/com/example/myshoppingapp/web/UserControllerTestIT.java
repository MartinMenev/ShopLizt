package com.example.myshoppingapp.web;

import com.example.myshoppingapp.model.enums.UserRole;
import com.example.myshoppingapp.model.pictures.ImageDownloadModel;
import com.example.myshoppingapp.model.pictures.ImageEntity;
import com.example.myshoppingapp.model.roles.RoleEntity;
import com.example.myshoppingapp.model.users.UserEntity;
import com.example.myshoppingapp.model.users.UserInputDTO;
import com.example.myshoppingapp.model.users.UserOutputDTO;
import com.example.myshoppingapp.service.RecipeService;
import com.example.myshoppingapp.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTestIT {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService mockUserService;



    private UserOutputDTO testUser;
    private ImageEntity testImage;
    @BeforeEach
    void setUp() {
        testImage = new ImageEntity();
        testUser = new UserOutputDTO();
        testUser.setId(1L);
        testUser.setUsername("martin");
        testUser.setEmail("martin@abv.bg");
        testUser.setImageEntity(testImage);
        RoleEntity testRole = new RoleEntity();
        testRole.setRole(UserRole.USER);
        testUser.setRoles(List.of(testRole));

        when(mockUserService.getLoggedUserDTO()).thenReturn(testUser);
    }

    @Test
    @WithMockUser(
            username = "martin",
            roles = {"ADMIN", "USER"}
    )
    void testShowProfilePage() throws Exception {
        when(mockUserService.getLoggedUserDTO()).thenReturn(testUser);

        mockMvc.perform(get("/user/profile")).
                andExpect(status().isOk()).
                andExpect(view().name("user/profile"));
    }
    @Test
    @WithMockUser(
            username = "martin",
            roles = {"ADMIN", "USER"}
    )
    void testShowProfileUpdatePage() throws Exception {
        mockMvc.perform(get("/update-user")).
                andExpect(status().isOk()).
                andExpect(view().name("user/update-user"));
    }

    @Test
    @WithMockUser(
            username = "martin",
            roles = {"ADMIN", "USER"}
    )
    void testUpdateUserProfile() throws Exception {
        UserInputDTO existingUser = new UserInputDTO();
        UserEntity user = new UserEntity();
        when(mockUserService.updateUser(existingUser)).thenReturn(user);

        mockMvc.perform(patch("/update-user").
                param("username", "martin123").
                param("email", "").
                with(csrf())).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("/users/login"));
    }

    @Test
    @WithMockUser(
            username = "martin",
            roles = {"ADMIN", "USER"}
    )
    void testDeleteUserSuccessfully() throws Exception {
        mockMvc.perform(delete("/delete-profile")
                        .with(csrf())).
                andExpect(status().is3xxRedirection()).
                andExpect((redirectedUrl("/")));
    }

    @Test
    @WithMockUser(
            username = "martin",
            roles = {"ADMIN", "USER"}
    )
    void testMakeUserAdmin() throws Exception {
        mockMvc.perform(get("/make-admin/{id}",testUser.getId())).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("/admin"));
    }



}