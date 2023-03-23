package com.example.myshoppingapp.web;

import com.example.myshoppingapp.model.users.UserInputDTO;
import com.example.myshoppingapp.model.users.UserOutputDTO;
import com.example.myshoppingapp.service.PictureService;
import com.example.myshoppingapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UserController {

    private final UserService userService;
    private final PictureService pictureService;


    @Autowired
    public UserController(UserService userService, PictureService pictureService) {
        this.userService = userService;
        this.pictureService = pictureService;
    }




    @GetMapping("user/profile")
    public String ShowUserProfile(Model model){
        UserOutputDTO userOutputDTO = this.userService.getLoggedUserDTO();
        model.addAttribute("user", userOutputDTO);
        return "user/profile";
    }

    @GetMapping("/update-user")
    public String updateProfile(Model model) {
        UserOutputDTO currentUser = this.userService.getLoggedUserDTO();
        model.addAttribute("user", currentUser);
        return "user/update-user";
    }

    @PatchMapping("/update-user")
    public String doUpdateProduct(UserInputDTO userInputDTO) {
        userService.updateUser(userInputDTO);
        return "redirect:/users/login";
    }

    @DeleteMapping("/delete-profile")
    public String deleteUser() {
        userService.deleteUserEntityById();
        return "redirect:/";
    }

    @GetMapping("/make-admin/{id}")
    public String makeUserAdmin(@PathVariable(value = "id") Long id) {
        this.userService.makeUserAdmin(id);
        return "redirect:/admin";
    }

}
