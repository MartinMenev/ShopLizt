package com.example.myshoppingapp.web;

import com.example.myshoppingapp.service.CommentService;
import com.example.myshoppingapp.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
        model.addAttribute("users", this.userService.findAllUserNotAdmin());
        return "user/admin";
    }



}
