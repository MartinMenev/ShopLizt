package com.example.myshoppingapp.web;

import com.example.myshoppingapp.model.comments.Comment;
import com.example.myshoppingapp.model.comments.OutputCommentDTO;
import com.example.myshoppingapp.model.enums.Category;
import com.example.myshoppingapp.model.pictures.ImageEntity;
import com.example.myshoppingapp.model.products.InputProductDTO;
import com.example.myshoppingapp.model.recipes.OutputRecipeDTO;
import com.example.myshoppingapp.model.recipes.Recipe;
import com.example.myshoppingapp.model.users.UserEntity;
import com.example.myshoppingapp.model.users.UserOutputDTO;
import com.example.myshoppingapp.service.CommentService;
import com.example.myshoppingapp.service.ProductService;
import com.example.myshoppingapp.service.RecipeService;
import com.example.myshoppingapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTestIT {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService mockUserService;
    @MockBean
    private ProductService mockProductService;

    @MockBean
    private RecipeService mockRecipeService;

    @MockBean
    private CommentService mockCommentService;

    private Recipe testRecipe;
    private OutputCommentDTO testComment;
    private OutputRecipeDTO testRecipeDTO;
    private InputProductDTO testProductDTO;
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


    }

    @Test
    @WithMockUser(
            username = "martin",
            roles = {"ADMIN", "USER"}
    )
    void testShowHomePage() throws Exception {
        UserOutputDTO testUser = new UserOutputDTO();
        testUser.setUsername("martin");
        when(mockUserService.getLoggedUserDTO(testUser.getUsername())).thenReturn(testUser);

        mockMvc.perform(get("/home")).
                andExpect(status().isOk()).
                andExpect(view().name("home"));
    }

    @Test
    @WithMockUser(
            username = "martin",
            roles = {"ADMIN", "USER"}
    )
    void testShowIndexPage() throws Exception {

        mockMvc.perform(get("/")).
                andExpect(status().isOk()).
                andExpect(view().name("index"));
    }

}