package com.example.myshoppingapp.service;

import com.example.myshoppingapp.exceptions.ObjectNotFoundException;
import com.example.myshoppingapp.model.enums.UserRole;
import com.example.myshoppingapp.model.products.InputProductDTO;
import com.example.myshoppingapp.model.products.OutputProductDTO;
import com.example.myshoppingapp.model.products.Product;
import com.example.myshoppingapp.model.roles.RoleEntity;
import com.example.myshoppingapp.model.users.UserEntity;
import com.example.myshoppingapp.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private UserService mockUserService;
    @Mock
    private ProductRepository mockProductRepository;
    @Spy
    ModelMapper modelMapper = new ModelMapper();

    private ProductService toTest;

    private Product testProduct;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {

        toTest = new ProductService(mockUserService, mockProductRepository, modelMapper);

        testProduct = new Product("potato");
        testProduct.setId(1L);

        testUser = new UserEntity()
                .setUsername("martin")
                .setEmail("martin@test.bg");
        testUser.setId(1L);

    }

    @Test
    public void testToAddProduct() {
        InputProductDTO testInputProductDTO = new InputProductDTO();
        testInputProductDTO.setId(1L);
        Product resulted = modelMapper.map(testInputProductDTO, Product.class);
        when(mockUserService.getLoggedUser(testUser.getUsername())).thenReturn(testUser);

        assertEquals(resulted.getId(), toTest.addProduct(testInputProductDTO, testUser.getUsername()).getId());
        assertEquals(resulted.getName(), toTest.addProduct(testInputProductDTO, testUser.getUsername()).getName());


    }

    @Test
    public void testToGetAllProducts() {
        List<Product> testList = new ArrayList<>();
        testProduct.setPosition(1L);
        testList.add(testProduct);
        when(mockUserService.getLoggedUserId(testUser.getUsername())).thenReturn(testUser.getId());
        when(mockProductRepository.findAllByUserEntityId(testUser.getId())).thenReturn(Optional.of(testList));

        OutputProductDTO testOutputProductDTO = modelMapper.map(testProduct, OutputProductDTO.class);
        List<OutputProductDTO> testListDTO = new ArrayList<>();
        testListDTO.add(testOutputProductDTO);

           assertEquals(testListDTO.size(), toTest.getListedProducts(testUser.getUsername()).size());
           assertEquals(testListDTO.get(0).getId(), toTest.getListedProducts(testUser.getUsername()).get(0).getId());
           assertEquals(testListDTO.get(0).getName(), toTest.getListedProducts(testUser.getUsername()).get(0).getName());

    }

    @Test
    public void testToUpdateProduct() {
        InputProductDTO testInputProduct = modelMapper.map(testProduct, InputProductDTO.class);
        testInputProduct.setName("I am a new product name");

        assertEquals(testInputProduct.getName(), toTest.updateProduct(testInputProduct));
        verify(mockProductRepository).updateProductName(testInputProduct.getId(), testInputProduct.getName());
    }


    @Test
    public void testToDeleteProductByCreator() {
        testProduct.setUserEntity(testUser);

        when(mockUserService.getLoggedUser(testUser.getUsername())).thenReturn(testUser);
        when(mockProductRepository.getReferenceById(testProduct.getId())).thenReturn(testProduct);


        assertEquals(testProduct.getId(), toTest.deleteById(testProduct.getId(), testUser.getUsername()));

    }

    @Test
    public void testToDeleteProductByAdmin() {
        testUser.addRole(new RoleEntity(1L, UserRole.ADMIN));
        testProduct.setUserEntity(new UserEntity());

        when(mockUserService.getLoggedUser(testUser.getUsername())).thenReturn(testUser);
        when(mockProductRepository.getReferenceById(testProduct.getId())).thenReturn(testProduct);

        assertEquals(testProduct.getId(), toTest.deleteById(testProduct.getId(), testUser.getUsername()));

    }

    @Test
    public void testShouldFailIfNonAdminNonCreatorTriesToDeleteProduct() {
        testUser.addRole(new RoleEntity(1L, UserRole.USER));
        testProduct.setUserEntity(new UserEntity());

        verify(mockProductRepository, times(0)).deleteById(testProduct.getId());

    }





    @Test
    public void testToGetProductById() {
        when(mockProductRepository.getProductById(testProduct.getId())).thenReturn(Optional.of(testProduct));

        OutputProductDTO testProductOutputDTO = modelMapper.map(testProduct, OutputProductDTO.class);

        assertEquals(testProductOutputDTO.getName(), toTest.getProductById(testProduct.getId()).getName());
        assertEquals(testProductOutputDTO.getId(), toTest.getProductById(testProduct.getId()).getId());

        assertThrows(ObjectNotFoundException.class,
                () -> toTest.getProductById(113L));
    }

    @Test
    public void testToBuyProduct() {
        when(mockProductRepository.getProductById(testProduct.getId())).thenReturn(Optional.of(testProduct));
        when(mockUserService.getLoggedUser(testUser.getUsername())).thenReturn(testUser);

        assertEquals(testUser.getUsername(), toTest.buyProduct(testProduct.getId(), testUser.getUsername()).getUsername());

        assertThrows(NoSuchElementException.class,
                ()-> toTest.buyProduct(113L, testUser.getUsername()));
    }

    @Test
    public void testToShowBoughtProducts() {
        List<OutputProductDTO> testOutputList = new ArrayList<>();

        OutputProductDTO testOutputProduct = modelMapper.map(testProduct, OutputProductDTO.class);
        testOutputList.add(testOutputProduct);
        Product product = modelMapper.map(testOutputProduct, Product.class);
        List<Product> testProductList = new ArrayList<>();
        testProductList.add(product);

        when(mockUserService.getLoggedUserId(testUser.getUsername())).thenReturn(testUser.getId());
        when(mockProductRepository.findAllByBuyerId(testUser.getId())).thenReturn(Optional.of(testProductList));

        assertEquals(testOutputList.size(), toTest.showBoughtProducts(testUser.getUsername()).size());
        assertEquals(testOutputList.get(0).getId(), toTest.showBoughtProducts(testUser.getUsername()).get(0).getId());

    }

    @Test
    public void testToReuseProduct() {
        testProduct
                .setBuyer(testUser);

        when(mockProductRepository.getProductById(testProduct.getId())).thenReturn(Optional.of(testProduct));

        toTest.reuseProduct(testProduct.getId());
        assertNull(testProduct.getBuyer());

    }


}