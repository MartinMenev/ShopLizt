package com.example.myshoppingapp.service;

import com.example.myshoppingapp.model.products.InputProductDTO;
import com.example.myshoppingapp.model.products.OutputProductDTO;
import com.example.myshoppingapp.model.products.Product;
import com.example.myshoppingapp.model.users.UserEntity;
import com.example.myshoppingapp.repository.ProductRepository;
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
import static org.mockito.Mockito.when;

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
        testProduct.setPosition(1);
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




}