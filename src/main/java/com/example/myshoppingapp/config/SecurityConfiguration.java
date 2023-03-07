package com.example.myshoppingapp.config;

import com.example.myshoppingapp.repository.UserRepository;
import com.example.myshoppingapp.service.ApplicationUserDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
public class SecurityConfiguration {



        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.
             authorizeHttpRequests().
                    requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll().
                    antMatchers("/", "/users/register", "/users/login", "/users/login-error").permitAll().
                    antMatchers("/admin").hasRole("ADMIN").
                    anyRequest().authenticated().
                    and().
                    formLogin().
                    loginPage("/users/login").
                    usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY).
                    passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY).
                    defaultSuccessUrl("/home", true).
                    failureForwardUrl("/users/login-error").
                    and().logout().//configure logout
                    logoutUrl("/users/logout").
                    logoutSuccessUrl("/").//go to homepage after logout
                    deleteCookies("JSESSIONID").
                    clearAuthentication(true).
                    and().
                    csrf();

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
