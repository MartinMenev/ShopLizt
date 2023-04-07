package com.example.myshoppingapp.web;

import com.example.myshoppingapp.model.products.dto.InputProductDTO;
import com.example.myshoppingapp.model.products.dto.OutputProductDTO;
import com.example.myshoppingapp.service.ProductService;
import com.example.myshoppingapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public ProductController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/product-list")
    public String showProductListPage(Model model, Principal user) {
        List<OutputProductDTO> products = this.productService.getListedProducts(user.getName());
        model.addAttribute("products", products);
        model.addAttribute("boughtProducts", this.productService.showBoughtProducts(user.getName()));
        return "product/product-list";
    }

    @PostMapping("/add-product")
    public String doAddProduct(InputProductDTO inputProductDTO, Principal user) {
        productService.addProduct(inputProductDTO, user.getName());
        return "redirect:/product-list";
    }

    @GetMapping("/product/updateProduct/{id}")
    public String updateProduct(@PathVariable(value = "id") Long id, Model model) {
        OutputProductDTO outputProductDTO = productService.getProductById(id);
        model.addAttribute("product", outputProductDTO);
        return "product/updateProduct";
    }

    @PutMapping ("/product/updateProduct/{id}")
    public String doUpdateProduct(@PathVariable(value = "id") Long id, Model model, InputProductDTO inputProductDTO) {
        OutputProductDTO outputProductDTO = productService.getProductById(id);
        model.addAttribute("product", outputProductDTO);
        productService.updateProduct(inputProductDTO);
        return "redirect:/product-list";
    }

    @GetMapping("/moveUpProduct/{position}")
    public String moveUpProduct(@PathVariable(value = "position") long position,
                                Principal user) {
        productService.moveUpProduct(position, user.getName());
        return "redirect:/product-list";
    }

    @GetMapping("/moveDownProduct/{position}")
    public String moveDownProduct(@PathVariable(value = "position") long position,
                                  Principal user) {
        productService.moveDownProduct(position, user.getName());
        return "redirect:/product-list";
    }


    @GetMapping("/deleteProduct/{id}")
    public String deleteById(@PathVariable(value = "id") long id,
                             Principal user) {
        productService.deleteById(id, user.getName());
        return "redirect:/product-list";
    }

    @GetMapping("/buy-product/{id}")
    public String buyProduct(@PathVariable(value = "id") long id,
                             Principal user) {
        productService.buyProduct(id, user.getName());
        return "redirect:/product-list";
    }

    @GetMapping("/reuse-product/{id}")
    public String reuseProduct(@PathVariable(value = "id") long id) {
        productService.reuseProduct(id);
        return "redirect:/product-list";
    }

}
