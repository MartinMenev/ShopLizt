package com.example.myshoppingapp.config;

import com.example.myshoppingapp.domain.beans.LoggedUser;
import com.example.myshoppingapp.repository.UserRepository;
import com.example.myshoppingapp.service.ApplicationUserDetailsService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.annotation.SessionScope;

@Configuration
public class SecurityConfiguration {

        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }

        @Bean
        @SessionScope
        public LoggedUser loggedUser() {
            return new LoggedUser();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.
             authorizeHttpRequests().
                    antMatchers("/", "/users/register", "/users/login", "/home").permitAll().
                    anyRequest().permitAll();
//                    and().
//                    formLogin().
//                    loginPage("/users/login").
//                    usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY).
//                    passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY).
//                    defaultSuccessUrl("/", true).
//                    failureForwardUrl("/users/login-error");

            return http.build();

        }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Pbkdf2PasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new ApplicationUserDetailsService(userRepository);
    }

}
