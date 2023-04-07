package com.example.myshoppingapp.service;

import com.example.myshoppingapp.exceptions.ObjectNotFoundException;
import com.example.myshoppingapp.model.enums.Category;
import com.example.myshoppingapp.model.pictures.ImageEntity;
import com.example.myshoppingapp.model.products.Product;
import com.example.myshoppingapp.model.recipes.dto.InputRecipeDTO;
import com.example.myshoppingapp.model.recipes.dto.OutputRecipeDTO;
import com.example.myshoppingapp.model.recipes.Recipe;
import com.example.myshoppingapp.model.users.UserEntity;
import com.example.myshoppingapp.repository.RecipeRepository;
import com.example.myshoppingapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import javax.transaction.NotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    private RecipeRepository mockRecipeRepository;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private UserService mockUserService;
    @Spy
    ModelMapper modelMapper = new ModelMapper();
    @Mock
    private ProductService mockProductService;
    @Mock
    private ImageService mockImageService;
    @Mock
    private CommentService mockCommentService;

    private RecipeService toTest;
    private Recipe testRecipe;
    private String name;

    private UserEntity testAuthor;

    @BeforeEach
    void setUp() {
        toTest = new RecipeService(mockRecipeRepository, mockUserRepository, mockUserService,
                modelMapper, mockProductService, mockImageService, mockCommentService);


        testRecipe = new Recipe()
                .setAuthor(new UserEntity())
                .setName("Musaka")
                .setCategory(Category.DINNER);
        testRecipe.setId(1L);

        name = "martin";

        testAuthor = new UserEntity();
        testAuthor.setUsername("martin");
        testAuthor.setId(3L);


    }

    @Test
    public void testAddingRecipe() {
        InputRecipeDTO inputRecipeDTO = new InputRecipeDTO();
        inputRecipeDTO.setId(1L);

        when(mockUserService.getLoggedUser(name)).thenReturn(new UserEntity());

        assertEquals(inputRecipeDTO.getId(), toTest.addRecipe(inputRecipeDTO, name));

    }

    @Test
    public void testShowLast5Recipes() {
        UserEntity anotherAuthor = new UserEntity();
        anotherAuthor.setId(1L);
        anotherAuthor.setUsername("pesho");

        List<Recipe> recipesToShow = new ArrayList<>();
        Recipe recipe1 = new Recipe().setAuthor(anotherAuthor);
        recipe1.setId(1L);
        Recipe recipe2 = new Recipe().setAuthor(anotherAuthor);
        recipe1.setId(2L);
        recipesToShow.add(recipe1);
        recipesToShow.add(recipe2);

        when(mockRecipeRepository.findAll()).thenReturn(recipesToShow);

        when(mockUserService.getLoggedUser(name)).thenReturn(testAuthor);

        List<OutputRecipeDTO> resultedRecipeList = toTest.showLast5Recipes(name);

        assertEquals(2, resultedRecipeList.size());

    }

    @Test
    public void testToGetRecipeById() {
        when(mockRecipeRepository.getRecipeById(testRecipe.getId())).thenReturn(Optional.of(testRecipe));

        assertEquals(testRecipe.getId(), toTest.getRecipeById(testRecipe.getId()).getId());

    }

    @Test
    public void testIfThrowsGettingWrongRecipeById() {
        when(mockRecipeRepository.getRecipeById(0L)).thenThrow(ObjectNotFoundException.class);

        assertThrows(ObjectNotFoundException.class,
                ()-> toTest.getRecipeById(0L));

    }


    @Test
    public void testToAddRecipeRating() {
        Long testRating = 4L;
        when(mockRecipeRepository.getById(testRecipe.getId())).thenReturn(testRecipe);
        assertEquals(testRating, toTest.addRecipeRating(testRating, testRecipe.getId()));

    }

    @Test
    public void testShowRecipesOfLoggedUser() {
        UserEntity anotherAuthor = new UserEntity();
        anotherAuthor.setId(1L);
        anotherAuthor.setUsername("pesho");

        List<Recipe> recipesToShow = new ArrayList<>();
        Recipe recipe1 = new Recipe().setAuthor(testAuthor);
        recipe1.setId(1L);
        Recipe recipe2 = new Recipe().setAuthor(testAuthor);
        recipe1.setId(2L);
        recipesToShow.add(recipe1);
        recipesToShow.add(recipe2);

        when(mockUserService.getLoggedUserFavoriteList(name)).thenReturn(recipesToShow);

        when(mockRecipeRepository.findAllByAuthorOrderByIdDesc(testAuthor)).thenReturn(recipesToShow);

        when(mockUserService.getLoggedUser(name)).thenReturn(testAuthor);

        List<OutputRecipeDTO> resultedRecipeList = toTest.showRecipesByLoggedUser(name);

        assertEquals(4, resultedRecipeList.size());

    }

    @Test
    public void testGetRecipesByCategory() {

        List<Recipe> recipesToShow = new ArrayList<>();
        Recipe recipe1 = new Recipe().setAuthor(testAuthor);
        recipe1.setId(1L);
        Recipe recipe2 = new Recipe().setAuthor(testAuthor);
        recipe1.setId(2L);
        recipesToShow.add(recipe1);
        recipesToShow.add(recipe2);

        when(mockRecipeRepository.findAllByCategory(Category.DINNER)).thenReturn(Optional.of(recipesToShow));

        List<OutputRecipeDTO> resultedList = toTest.getRecipesByCategory(String.valueOf(Category.DINNER));

        assertEquals(2, resultedList.size());
    }

    @Test
    public void testGetRecipesByTextContent() {

        List<Recipe> recipesToShow = new ArrayList<>();
        Recipe recipe1 = new Recipe().setAuthor(testAuthor);
        recipe1.setId(1L);
        Recipe recipe2 = new Recipe().setAuthor(testAuthor);
        recipe1.setId(2L);
        recipesToShow.add(recipe1);
        recipesToShow.add(recipe2);

        String text = "searchText";

        when(mockRecipeRepository.findAllContainingSearchText(text)).thenReturn(Optional.of(recipesToShow));

        List<OutputRecipeDTO> resultedList = toTest.getRecipesByTextContent(text);

        assertEquals(2, resultedList.size());
    }

    @Test
    public void testToDeleteRecipeSuccessfullyByAuthor() throws NotSupportedException {
        testRecipe.setAuthor(testAuthor);

        when(mockUserService.getLoggedUser(name)).thenReturn(testAuthor);

        when(mockRecipeRepository.findById(testRecipe.getId())).thenReturn(Optional.of(testRecipe));

        when(mockUserRepository.findAllByFavoriteRecipesContains(testRecipe)).thenReturn(Optional.of(List.of(testAuthor)));

       toTest.deleteById(testRecipe.getId(), testAuthor.getUsername());

       verify(mockRecipeRepository).delete(any());
    }

    @Test
    public void testThrowWhenNonAdminNonAuthorTryToDeleteRecipe() throws NotSupportedException {
        testRecipe.setAuthor(testAuthor);

        UserEntity loggedUser = new UserEntity();
        loggedUser.setUsername("pesho");
        loggedUser.setId(143L);

        when(mockUserService.getLoggedUser(loggedUser.getUsername())).thenReturn(loggedUser);

        when(mockRecipeRepository.findById(testRecipe.getId())).thenReturn(Optional.of(testRecipe));

        assertThrows(NotSupportedException.class,
                () -> toTest.deleteById(testRecipe.getId(), loggedUser.getUsername()));

        verify(mockRecipeRepository, times(0)).delete(any());

    }

    @Test
    public void testToUpdateRecipe() {
        InputRecipeDTO updatedRecipeDTO = new InputRecipeDTO();
        updatedRecipeDTO.setId(1L);
        updatedRecipeDTO.setImageUrl("something");
        updatedRecipeDTO.setName("updatedName");
        updatedRecipeDTO.setCategory(Category.BAKED);
        when(mockRecipeRepository.getRecipeById(testRecipe.getId())).thenReturn(Optional.of(testRecipe));

        assertEquals(updatedRecipeDTO.getName(), toTest.updateRecipe(updatedRecipeDTO).getName());
        verify(mockRecipeRepository).save(any());
    }

    @Test
    public void testAddingProductToRecipe() {
        Product testProduct = new Product("bread");

        when(mockRecipeRepository.findById(testRecipe.getId())).thenReturn(Optional.of(testRecipe));

        assertEquals(testProduct.getId(), toTest.addProductToRecipe(testRecipe.getId(), testProduct.getName()).getId());
        verify(mockRecipeRepository).saveAndFlush(any());
        verify(mockProductService).saveProduct(any());
    }

    @Test
    public void testAddProductToMyList() {
        String name = "tomato";
        assertEquals(name, toTest.addProductToMyList(name, testAuthor.getUsername()));
    }

    @Test
    public void testToDeleteProductFromRecipe() {
        Product testProduct = new Product();
        testRecipe.getProductList().add(testProduct);

        when(mockRecipeRepository.findById(testRecipe.getId())).thenReturn(Optional.of(testRecipe));
        when(mockProductService.findProductById(testProduct.getId())).thenReturn(testProduct);

        toTest.deleteProductFromRecipe(testRecipe.getId(), testProduct.getId());

        assertFalse(testRecipe.getProductList().contains(testProduct));
        verify(mockRecipeRepository).saveAndFlush(testRecipe);
    }

    @Test
    public void testToAddAllProductsToMyList() {
        Product testProduct = new Product("bread");
        testRecipe.getProductList().add(testProduct);

        when(mockRecipeRepository.getRecipeById(testRecipe.getId())).thenReturn(Optional.of(testRecipe));
        toTest.addAllProductsToMyList(testRecipe.getId(), testAuthor.getUsername());

        verify(mockProductService).addProductToMyList(testProduct.getName(), testAuthor.getUsername());

    }


    @Test
    public void testToSaveRecipeToMyFavoriteList() {
        UserEntity testUser = new UserEntity();
        testUser.setUsername("martin");

        when(mockRecipeRepository.findById(testRecipe.getId())).thenReturn(Optional.of(testRecipe));
        when(mockUserService.addRecipeToFavoriteList(testRecipe, testUser.getUsername())).thenReturn(testRecipe);

        long recipeNumberOfSaves = testRecipe.getNumberOfSaves();

        toTest.saveRecipeToMyFavoriteList(testRecipe.getId(), testUser.getUsername());

        assertEquals(recipeNumberOfSaves + 1, testRecipe.getNumberOfSaves());
        verify(mockUserService).addRecipeToFavoriteList(testRecipe, testUser.getUsername());

    }

    @Test
    public void testToAddImageToRecipe() {
        ImageEntity testImage = new ImageEntity();
        testImage.setId(1L);

        when(mockImageService.findById(testImage.getId())).thenReturn(Optional.of(testImage));

        when(mockRecipeRepository.findById(testRecipe.getId())).thenReturn(Optional.of(testRecipe));

        assertEquals(testImage,toTest.addImageToRecipe(testImage.getId(), testImage.getId()));

    }

    @Test
    public void testToSuccessfullyDeleteImageFromRecipe() {
        ImageEntity testImage = new ImageEntity();
        testRecipe.getImageList().add(testImage);
        assertTrue(testRecipe.getImageList().contains(testImage));

        when(mockRecipeRepository.getRecipeById(testRecipe.getId())).thenReturn(Optional.of(testRecipe));
        toTest.removeImageFromRecipe(testRecipe.getId(), testImage);

        assertFalse(testRecipe.getImageList().contains(testImage));
        verify(mockRecipeRepository, times(1)).saveAndFlush(testRecipe);
        verify(mockImageService, times(1)).delete(testImage);

    }

}