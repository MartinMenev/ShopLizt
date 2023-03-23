package com.example.myshoppingapp.web;

import com.example.myshoppingapp.model.BaseEntity;
import com.example.myshoppingapp.model.comments.Comment;
import com.example.myshoppingapp.model.comments.OutputCommentDTO;
import com.example.myshoppingapp.model.enums.Category;
import com.example.myshoppingapp.model.pictures.ImageEntity;
import com.example.myshoppingapp.model.products.InputProductDTO;
import com.example.myshoppingapp.model.products.Product;
import com.example.myshoppingapp.model.recipes.OutputRecipeDTO;
import com.example.myshoppingapp.model.recipes.Recipe;
import com.example.myshoppingapp.model.users.UserEntity;
import com.example.myshoppingapp.service.CommentService;
import com.example.myshoppingapp.service.EmailService;
import com.example.myshoppingapp.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.OngoingStubbing;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class RecipeControllerTestIT {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService mockRecipeService;
    @MockBean
    private CommentService mockCommentService;


    private Recipe testRecipe;
    private OutputCommentDTO testComment;
    private OutputRecipeDTO testRecipeDTO;
    private InputProductDTO testProductDTO;

    private ImageEntity testImage;
    @BeforeEach
    void setUp(){
        ModelMapper modelMapper = new ModelMapper();
        testRecipe = new Recipe();
        testRecipe.setId(3L);
        testRecipe.setName("Musaka").setCategory(Category.DINNER);

        UserEntity author = new UserEntity().setUsername("martin");
        ImageEntity testImage = new ImageEntity();
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setRecipe(testRecipe);
        testComment = modelMapper.map(comment, OutputCommentDTO.class);
        testRecipe.setId(3L);
        testRecipe.setAuthor(author);
        testRecipe.setUrl("someUrl");
        testRecipe.setAuthor(author);
        testRecipe.addImage(testImage);
        testRecipeDTO = modelMapper.map(testRecipe, OutputRecipeDTO.class);
        testProductDTO = new InputProductDTO();
        testProductDTO.setName("bread");
    }
    @Test
    @WithMockUser(
            username = "martin",
            roles = {"ADMIN", "USER"}
    )
    void testShowAddRecipePage() throws Exception {
        mockMvc.perform(get("/add-recipe")).
                andExpect(status().isOk()).
                andExpect(view().name("recipe/add-recipe"));
    }

    @Test
    @WithMockUser(
            username = "martin",
            roles = {"ADMIN", "USER"}
    )
    void testDeletingRecipeSuccessfully() throws Exception {

        mockMvc.perform(delete("/delete-recipe/{id}", testRecipe.getId())
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/all-recipes"));
    }

    @Test
    void testDeleteRecipeByAnonymous() throws Exception {

        mockMvc.perform(delete("/delete-recipe/{id}", testRecipe.getId())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(
            username = "admin@example.com",
            roles = {"ADMIN", "USER"}
    )
    void testAddRecipeSuccessfulPosting() throws Exception {
        mockMvc.perform(post("/add-recipe")
                .param("name", "Musaka")
                .param("description", "test description")
                .param("category", "DINNER")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my-collection"));

    }

    @Test
    @WithMockUser(
            username = "martin",
            roles = {"ADMIN", "USER"}
    )
    void testPostingRecipeWithIncompleteData() throws Exception {
        mockMvc.perform(post("/add-recipe")
                        .param("name", "Musaka")
                        .param("description", "")
                        .param("category", "")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/add-recipe"));

    }

    @Test
    @WithMockUser(
            username = "martin",
            roles = {"ADMIN", "USER"}
    )
    void testSavingRecipeToFavorites() throws Exception {
        mockMvc.perform(get("/save-recipe-to-favorites/{id}", testRecipe.getId())
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    @WithMockUser(
            username = "martin",
            roles = {"ADMIN", "USER"}
    )
    void testGetRecipeById() throws Exception {
        when(mockRecipeService.getRecipeById(testRecipeDTO.getId())).thenReturn(testRecipeDTO);
        when(mockCommentService.showLatestComments(testRecipe.getId())).thenReturn(List.of(testComment));

        mockMvc.perform(get("/recipe/{id}", testRecipeDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipe-details"));
    }

    @Test
    @WithMockUser(
            username = "martin",
            roles = {"ADMIN", "USER"}
    )
    void testAddingProductToRecipeFailed() throws Exception {
        mockMvc.perform(post("/add-product-to-recipe/{id}/{name}",
                        testRecipe.getId(), testProductDTO.getName())
                        .with(csrf()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(
            username = "martin",
            roles = {"ADMIN", "USER"}
    )
    void testShowEditRecipePage() throws Exception {
        when(mockRecipeService.getRecipeById(3L)).thenReturn(testRecipeDTO);
        mockMvc.perform(get("/edit-recipe/{id}", testRecipeDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("recipe", testRecipeDTO))
                .andExpect(model().attribute("pictures", testRecipeDTO.getPictureList()))
                .andExpect(view().name("recipe/update-recipe"));
    }
}