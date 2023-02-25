package com.example.myshoppingapp.validation;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

    private String first;
    private String second;
    private String message;


    @Override
    public void initialize(FieldMatch constraintAnnotation) {
      this.first = constraintAnnotation.first();
      this.second = constraintAnnotation.second();
      this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {

      BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(value);

      Object passwordValue = beanWrapper.getPropertyValue(this.first);
      Object confirmPasswordValue = beanWrapper.getPropertyValue(this.second);


      if (passwordValue != null && passwordValue.equals(confirmPasswordValue)) {
        return true;
      }

      constraintValidatorContext.buildConstraintViolationWithTemplate(message)
              .addPropertyNode(second)
              .addConstraintViolation()
              .disableDefaultConstraintViolation();


      return false;

  }
}
