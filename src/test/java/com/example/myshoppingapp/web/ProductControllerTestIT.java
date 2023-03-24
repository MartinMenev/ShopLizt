package com.example.myshoppingapp.web;

import com.example.myshoppingapp.model.products.InputProductDTO;
import com.example.myshoppingapp.model.products.OutputProductDTO;
import com.example.myshoppingapp.model.products.Product;
import com.example.myshoppingapp.service.ProductService;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTestIT {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService mockProductService;
    private InputProductDTO testProduct;

    @BeforeEach
    void setUp(){
        ModelMapper modelMapper = new ModelMapper();
        Product product = new Product();
        product.setId(1L);
        product.setName("tomato 1kg");
        product.setPosition(1L);
        testProduct = modelMapper.map(product, InputProductDTO.class);

    }

    @Test
    @WithMockUser(
            username = "martin",
            roles = {"ADMIN", "USER"}
    )
    void testShowProductList() throws Exception {
        mockMvc.perform(get("/product-list")).
                andExpect(status().isOk()).
                andExpect(view().name("product/product-list"));
    }

    @Test
    void testToShowProductListForAnonymousUser() throws Exception {
        mockMvc.perform(get("/product-list")).
                andExpect(status().is3xxRedirection());

    }

    @Test
    @WithMockUser(
            username = "martin",
            roles = {"ADMIN", "USER"}
    )
    void testAddingProductToMyList() throws Exception {
        mockMvc.perform(post("/add-product")
                        .param("name", "bread")
                                .with(csrf())).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("/product-list"));
    }

    @Test
    @WithMockUser(
            username = "martin",
            roles = {"ADMIN", "USER"}
    )
    void testShowUpdateProductPage() throws Exception {
        OutputProductDTO product = new OutputProductDTO();
        when(mockProductService.getProductById(testProduct.getId())).thenReturn(product);
        mockMvc.perform(get("/product/updateProduct/{id}", testProduct.getId())).
                andExpect(model().attribute("product", product)).
                andExpect(status().isOk()).
                andExpect(view().name("product/updateProduct"));
    }

    @Test
    @WithMockUser(
            username = "martin",
            roles = {"ADMIN", "USER"}
    )
    void testToUpdateProduct() throws Exception {
        OutputProductDTO product = new OutputProductDTO();
        product.setId(1L);
        product.setName("tomato");
        when(mockProductService.getProductById(testProduct.getId())).thenReturn(product);
        mockMvc.perform(put("/product/updateProduct/{id}", testProduct.getId(), testProduct)
                        .with(csrf())).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("/product-list"));
    }

    @Test
    @WithMockUser(
            username = "martin",
            roles = {"ADMIN", "USER"}
    )
    void testToMoveUpProduct() throws Exception {
        mockMvc.perform(get("/moveUpProduct/{position}", 1)
                        .with(csrf())).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("/product-list"));
    }

    @Test
    @WithMockUser(
            username = "martin",
            roles = {"ADMIN", "USER"}
    )
    void testToMoveDownProduct() throws Exception {
        mockMvc.perform(get("/moveDownProduct/{position}",1)
                        .with(csrf())).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("/product-list"));
    }

    @Test
    @WithMockUser(
            username = "martin",
            roles = {"ADMIN", "USER"}
    )
    void testToDeleteProduct() throws Exception {
        mockMvc.perform(get("/deleteProduct/{id}", testProduct.getId())
                        .with(csrf())).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("/product-list"));
    }

    @Test
    @WithMockUser(
            username = "martin",
            roles = {"ADMIN", "USER"}
    )
    void testToBuyProduct() throws Exception {
        mockMvc.perform(get("/buy-product/{id}", testProduct.getId())
                        .with(csrf())).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("/product-list"));
    }

    @Test
    @WithMockUser(
            username = "martin",
            roles = {"ADMIN", "USER"}
    )
    void testToReuseProduct() throws Exception {
        mockMvc.perform(get("/reuse-product/{id}", testProduct.getId())
                        .with(csrf())).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("/product-list"));
    }

}