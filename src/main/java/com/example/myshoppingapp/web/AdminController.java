package com.example.myshoppingapp.web;

import com.example.myshoppingapp.domain.products.OutputProductDTO;
import com.example.myshoppingapp.service.CommentService;
import com.example.myshoppingapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminController {

    private final CommentService commentService;
    private final UserService userService;

    public AdminController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }


    @GetMapping("/admin")
    public String showAdminPage(Model model) {
        model.addAttribute("comments", this.commentService.getAllUnapprovedComments());

        return "user/admin";
    }

    @PostMapping("/search-user")
    public String showSearchedUsers(Model model,
                                    @RequestParam(value = "text") String text){
        model.addAttribute("searchedUsers", this.userService.searchUsersByName(text));
        return "redirect:/admin";
    }


}
