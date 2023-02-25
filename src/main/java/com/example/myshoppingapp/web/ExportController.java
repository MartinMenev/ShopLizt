package com.example.myshoppingapp.web;

import com.example.myshoppingapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExportController {

    private final ProductService productService;

    @Autowired
    public ExportController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("export/export-all-products")
    public String getProducts(Model model) {
        String outputProducts = this.productService.getAllProducts();
        System.out.println("outputProducts...");
        model.addAttribute("showAllProducts", outputProducts);
        return "export/export-all-products";
    }

}
