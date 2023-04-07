package com.example.myshoppingapp.web;

import com.example.myshoppingapp.model.comments.Comment;
import com.example.myshoppingapp.model.comments.dto.OutputCommentDTO;
import com.example.myshoppingapp.model.enums.Category;
import com.example.myshoppingapp.model.pictures.ImageEntity;
import com.example.myshoppingapp.model.recipes.dto.OutputRecipeDTO;
import com.example.myshoppingapp.model.recipes.Recipe;
import com.example.myshoppingapp.model.users.UserEntity;
import com.example.myshoppingapp.service.CommentService;
import com.example.myshoppingapp.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTestIT {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CommentService mockCommentService;
    @MockBean
    private RecipeService mockRecipeService;

    private Recipe testRecipe;
    private OutputCommentDTO testComment;
    private OutputRecipeDTO testRecipeDTO;
    @BeforeEach
    void setUp(){
        UserEntity author = new UserEntity().setUsername("martin");
        ImageEntity testImage = new ImageEntity();
        ModelMapper modelMapper = new ModelMapper();
        testRecipe = new Recipe();
        testRecipe.setId(3L);
        testRecipe.setName("Musaka")
                .setCategory(Category.DINNER)
                .setId(3L);
        testRecipe.setAuthor(author);
        testRecipe.setUrl("someUrl");
        testRecipe.addImage(testImage);
        testRecipeDTO = modelMapper.map(testRecipe, OutputRecipeDTO.class);
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setRecipe(testRecipe);
        testComment = modelMapper.map(comment, OutputCommentDTO.class);
    }

    @Test
    @WithMockUser(
            username = "martin",
            roles = {"ADMIN", "USER"}
    )
    void testToSubmitComment() throws Exception {
        mockMvc.perform(post("/save-comment/{id}", testRecipe.getId())
                        .param("rating", "5").
                with(csrf())).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("/recipe/" + testRecipe.getId()));
    }

    @Test
    @WithMockUser(
            username = "martin",
            roles = {"ADMIN", "USER"}
    )
    void testToDeleteComment() throws Exception {
        mockMvc.perform(delete("/delete-comment/{id}", testRecipe.getId()).
                with(csrf())).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("/home"));
    }


    @Test
    @WithMockUser(
            username = "martin",
            roles = {"ADMIN", "USER"}
    )
    void testToGetAllComments() throws Exception {
        when(mockCommentService.showAllComments(testRecipe.getId())).thenReturn(List.of(testComment));
        when(mockRecipeService.getRecipeById(testRecipe.getId())).thenReturn(testRecipeDTO);
        mockMvc.perform(get("/comments/{id}", testRecipe.getId()).
                        with(csrf())).
                andExpect(model().attribute("recipe", testRecipeDTO)).
                andExpect(status().isOk()).
                andExpect(view().name("comment/all-comments"));
    }

    @Test
    @WithMockUser(
            username = "martin",
            roles = {"ADMIN", "USER"}
    )
    void testToApproveComment() throws Exception {
        mockMvc.perform(patch("/approve-comment/{id}", testRecipe.getId()).
                        with(csrf())).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("/admin"));
    }
}