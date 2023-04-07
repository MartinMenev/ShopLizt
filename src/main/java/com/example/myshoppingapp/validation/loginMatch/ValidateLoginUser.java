package com.example.myshoppingapp.validation.loginMatch;

import com.example.myshoppingapp.validation.loginMatch.LoginUserValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = LoginUserValidator.class)
public @interface ValidateLoginUser {

  String message() default "Username or Password doesn't match";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
