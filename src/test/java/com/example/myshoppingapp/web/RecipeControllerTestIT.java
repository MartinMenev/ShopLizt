package com.example.myshoppingapp.web;

import com.example.myshoppingapp.model.recipes.Recipe;
import com.example.myshoppingapp.service.EmailService;
import com.example.myshoppingapp.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@SpringBootTest
@AutoConfigureMockMvc
class RecipeControllerTestIT {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService mockRecipeService;

    @BeforeEach
    void setUp(){

    }
    @Test
    @WithMockUser(
            username = "admin@example.com",
            roles = {"ADMIN", "USER"}
    )
    void testShowAddRecipe() throws Exception {
        mockMvc.perform(get("/add-recipe")).
                andExpect(status().isOk()).
                andExpect(view().name("recipe/add-recipe"));
    }

    @Test
    @WithMockUser(
            username = "admin@example.com",
            roles = {"ADMIN", "USER"}
    )
    void testDeleteRecipe() throws Exception {
        Recipe testRecipe = new Recipe();
        testRecipe.setId(3L);
        mockMvc.perform(delete("/delete-recipe/{id}", testRecipe.getId())
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/all-recipes"));
    }

}