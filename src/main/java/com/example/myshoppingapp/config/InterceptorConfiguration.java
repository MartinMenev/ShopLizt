package com.example.myshoppingapp.config;

import com.example.myshoppingapp.interceptor.LoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class InterceptorConfiguration extends WebMvcConfigurerAdapter {

    private final LocaleChangeInterceptor localeChangeInterceptor;

    public InterceptorConfiguration(LocaleChangeInterceptor localeChangeInterceptor) {
        this.localeChangeInterceptor = localeChangeInterceptor;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggingInterceptor());
        super.addInterceptors(registry);
        registry.addInterceptor(localeChangeInterceptor);

    }



}
