package com.example.myshoppingapp.web;

import com.example.myshoppingapp.model.users.dto.RegisterUserDTO;
import com.example.myshoppingapp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @GetMapping("/users/login")
    public String login() {
        return "user/login";
    }


    @PostMapping("/users/login-error")
    public String onFailedLogin(
            @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String username,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, username);
        redirectAttributes.addFlashAttribute("bad_credentials", true);

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


    @ModelAttribute(name = "registerUserDTO")
    public RegisterUserDTO initRegisterUserDTO() {
        return new RegisterUserDTO();
    }


}
