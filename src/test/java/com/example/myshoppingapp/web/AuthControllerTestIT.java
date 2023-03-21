package com.example.myshoppingapp.web;

import com.example.myshoppingapp.repository.UserRepository;
import com.example.myshoppingapp.service.ApplicationUserDetailsService;
import com.example.myshoppingapp.service.EmailService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTestIT {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmailService mockEmailService;


    @Test
    void testRegistrationPageShown() throws Exception {
        mockMvc.perform(get("/users/register")).
                andExpect(status().isOk()).
                andExpect(view().name("user/register"));
    }

    @Test
    void testLoginPageShown() throws Exception {
        mockMvc.perform(get("/users/login")).
                andExpect(status().isOk()).
                andExpect(view().name("user/login"));



    }




    @Test
    void testUserRegistration() throws Exception {
      mockMvc.perform(post("/users/register").
                        param("email", "anna@example.com").
                        param("username", "Anna1").
                        param("password", "11223344").
                        param("confirmPassword", "11223344").
                        with(csrf())).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("/users/login"));

        verify(mockEmailService).
                sendRegistrationEmail("anna@example.com",
                        "Anna1");

    }

    @Test
    void testUserRegistrationWithWrongInputs() throws Exception {
      mockMvc.perform(post("/users/register").
                        param("email", "anna@example.com").
                        param("username", "Anna").
                        param("password", "11223344").
                        param("confirmPassword", "1122339").
                        with(csrf())).
                andExpect(status().is3xxRedirection()).
                andExpect(redirectedUrl("/users/register"));




    }
}