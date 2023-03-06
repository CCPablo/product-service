package com.mercadona.product.ean.application.controller.validator;

import com.mercadona.product.ean.application.controller.dto.CreateProductRequestDTO;
import com.mercadona.product.ean.application.controller.dto.UpdateProductRequestDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ValidEanValidator implements ConstraintValidator<ValidEan, Object> {

    private final String VALID_EAN_REGEX = "\\d{13}";

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if(value instanceof UpdateProductRequestDTO dto) {
            return Pattern.matches(VALID_EAN_REGEX, dto.eanCode());
        } else if(value instanceof CreateProductRequestDTO dto) {
            return Pattern.matches(VALID_EAN_REGEX, dto.eanCode());
        }
        return true;
    }

}