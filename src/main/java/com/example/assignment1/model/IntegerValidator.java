package com.example.assignment1.model;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IntegerValidator implements ConstraintValidator<IntegerCheck, Integer>{


    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context){
        if(value == null){
            return true;
        }
        return value instanceof Integer;
    }
}
