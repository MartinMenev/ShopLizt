package com.example.myshoppingapp.service;

import com.example.myshoppingapp.model.comments.Comment;
import com.example.myshoppingapp.model.comments.InputCommentDTO;
import com.example.myshoppingapp.model.comments.OutputCommentDTO;
import com.example.myshoppingapp.model.enums.Category;
import com.example.myshoppingapp.model.enums.UserRole;
import com.example.myshoppingapp.model.recipes.Recipe;
import com.example.myshoppingapp.model.roles.RoleEntity;
import com.example.myshoppingapp.model.users.UserEntity;
import com.example.myshoppingapp.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    private CommentRepository mockCommentRepository;
    @Spy
    ModelMapper modelMapper = new ModelMapper();
    @Mock
    private UserService mockUserService;
    @Mock
    private RecipeService mockRecipeService;

    private CommentService toTest;

    private Comment testComment;

    private UserEntity testUser;

    private Recipe testRecipe;

    @BeforeEach
    void setUp(){

        toTest = new CommentService(mockCommentRepository, modelMapper, mockUserService, mockRecipeService);

        testUser = (UserEntity) new UserEntity()
                .setUsername("martin")
                .setId(2L);

        testRecipe = (Recipe) new Recipe()
                .setName("Musaka")
                .setCategory(Category.DINNER)
                .setAuthor(testUser)
                .setId(1L);

        testComment = (Comment) new Comment()
                .setRecipe(testRecipe)
                .setAuthor(testUser)
                .setText("test comment")
                .setRating(4)
                .setId(13L);


    }

    @Test
    public void testToSubmitCommentSuccessfully() {
        InputCommentDTO testInputComment = modelMapper.map(testComment, InputCommentDTO.class);

        when(mockUserService.getLoggedUser(testUser.getUsername())).thenReturn(testUser);
        when(mockRecipeService.getRecipeEntityById(testRecipe.getId())).thenReturn(Optional.of(testRecipe));
        Comment commentToSave = toTest.addComment(testInputComment, testRecipe.getId(), testUser.getUsername());

        verify(mockCommentRepository, times(1)).saveAndFlush(commentToSave);
        assertEquals(testComment.getId(), commentToSave.getId());
        assertEquals(testComment.getRating(), commentToSave.getRating());
        assertEquals(testComment.getAuthor(), commentToSave.getAuthor());
        assertEquals(testComment.getRecipe().getName(), commentToSave.getRecipe().getName());
    }


    @Test
    public void testToDeleteOwnComment() {
        when(mockCommentRepository.getReferenceById(testComment.getId())).thenReturn(testComment);
        when(mockUserService.getLoggedUser(testUser.getUsername())).thenReturn(testUser);

        toTest.deleteComment(testComment.getId(), testUser.getUsername());

        verify(mockCommentRepository).deleteById(testComment.getId());


    }

    @Test
    public void testIfAdminUserCanDeleteComment() {
        UserEntity anotherUser = new UserEntity()
                .setUsername("testUser");
        anotherUser.addRole(new RoleEntity(1L, UserRole.ADMIN));
        assertTrue(anotherUser.isAdmin());

        when(mockCommentRepository.getReferenceById(testComment.getId())).thenReturn(testComment);
        when(mockUserService.getLoggedUser(anotherUser.getUsername())).thenReturn(anotherUser);

        toTest.deleteComment(testComment.getId(), anotherUser.getUsername());

        verify(mockCommentRepository).deleteById(testComment.getId());

    }

    @Test
    public void testIfRegularUserCannotDeleteOthersComment() {
        UserEntity anotherUser = new UserEntity()
                .setUsername("testUser");
        anotherUser.addRole(new RoleEntity(1L, UserRole.USER));
        assertFalse(anotherUser.isAdmin());

        when(mockCommentRepository.getReferenceById(testComment.getId())).thenReturn(testComment);
        when(mockUserService.getLoggedUser(anotherUser.getUsername())).thenReturn(anotherUser);

        toTest.deleteComment(testComment.getId(), anotherUser.getUsername());

        verify(mockCommentRepository, times(0)).deleteById(testComment.getId());

    }

    @Test
    public void testToApproveCommentSuccessfully() {
        when(mockCommentRepository.getReferenceById(testComment.getId())).thenReturn(testComment);
        assertFalse(testComment.isApproved());

        assertTrue(toTest.approveComment(testComment.getId()).isApproved());

    }

    @Test
    public void testToShowAllComments() {
        List<Comment> testListComments = new ArrayList<>();
        testListComments.add(testComment);
        testListComments.add(testComment);

        when(mockCommentRepository.findAllByRecipeIdOrderByIdDesc(testRecipe.getId())).thenReturn(Optional.of(testListComments));

        OutputCommentDTO testOutputComment = modelMapper.map(testComment, OutputCommentDTO.class);
        List<OutputCommentDTO> testOutputList = new ArrayList<>();
        testOutputList.add(testOutputComment);
        testOutputList.add(testOutputComment);

        assertEquals(testOutputList.get(1).getText(), toTest.showAllComments(testRecipe.getId()).get(1).getText());
        assertEquals(testOutputList.get(1).getAuthor(), toTest.showAllComments(testRecipe.getId()).get(1).getAuthor());
        assertEquals(2, toTest.showAllComments(testRecipe.getId()).size());


    }

}