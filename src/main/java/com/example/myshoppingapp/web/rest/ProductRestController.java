package com.example.myshoppingapp.web.rest;

import com.example.myshoppingapp.model.products.OutputProductDTO;
import com.example.myshoppingapp.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class ProductRestController {
    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/api/bought-products")
    public ResponseEntity<List<OutputProductDTO>> getProductList(Principal user) {
        var products = productService.showBoughtProducts(user.getName());
        return ResponseEntity.ok(products);

    }
}
