package com.liulin.valid;

import lombok.val;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ListValuesConstraintValidator implements ConstraintValidator<ListValues, Integer> {
    Set<Integer> set = new HashSet<>();
    @Override
    public void initialize(ListValues constraintAnnotation) {
        val values = constraintAnnotation.values();
        for (Integer integer : values) {
            set.add(integer);
        }
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return set.contains(value);
    }
}
