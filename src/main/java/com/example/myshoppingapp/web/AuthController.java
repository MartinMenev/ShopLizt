package com.example.myshoppingapp.web;

import com.example.myshoppingapp.domain.users.LoginDTO;
import com.example.myshoppingapp.domain.users.RegisterUserDTO;
import com.example.myshoppingapp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @GetMapping("/users/login")
    public String login() {
        return "user/login";
    }

    @PostMapping("/users/login")
    public String doLogin(@Valid LoginDTO loginDTO,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes)  {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("loginDTO", loginDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.loginDTO",
                    bindingResult);
            return "user/login";
        }

        authService.login(loginDTO);
        return "redirect:/home";
    }


    @PostMapping("/users/logout")
    public String doLogout(){
        this.authService.logout();
        return "redirect:/users/login";
    }


    @GetMapping("/users/register")
    public String register() {
        return "user/register";
    }

    @PostMapping("/users/register")
    public String doRegister(@Valid  RegisterUserDTO registerUserDTO,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes ) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("registerUserDTO", registerUserDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registerUserDTO",
                    bindingResult);
            return "redirect:/users/register";
        }

        this.authService.register(registerUserDTO);

        return "redirect:/users/login";
    }

    @ModelAttribute("loginDTO")
    public LoginDTO initLoginDTO() {
        return new LoginDTO();
    }

    @ModelAttribute(name = "registerUserDTO")
    public RegisterUserDTO initRegisterUserDTO() {
        return new RegisterUserDTO();
    }


}
