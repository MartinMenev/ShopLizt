package com.example.myshoppingapp.web;

import com.example.myshoppingapp.domain.users.UserInputDTO;
import com.example.myshoppingapp.domain.users.UserOutputDTO;
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
        //TODO
//        String currentUrl = this.pictureService.getPictureUrlByLoggedUser();
//        model.addAttribute("pictureUrl", currentUrl);
        model.addAttribute("user", userOutputDTO);
        return "user/profile";
    }

    @GetMapping("/update-user")
    public String updateProduct(Model model) {
        UserOutputDTO currentUser = this.userService.getLoggedUserDTO();
        model.addAttribute("user", currentUser);
        return "user/update-user";
    }

    @PatchMapping("/update-user")
    public String doUpdateProduct(Model model, UserInputDTO userInputDTO) {
        UserOutputDTO currentUser = this.userService.getLoggedUserDTO();
        model.addAttribute("user", currentUser);
        userService.updateUser(userInputDTO);
        return "redirect:/users/login";
    }

    @DeleteMapping("/delete-profile/{id}")
    public String deleteById(@PathVariable(value = "id") long id) {
        userService.deleteUserEntityById(id);
        return "redirect:/";
    }

}
