package com.mercadona.product.ean.application.controller.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ValidEanValidator.class)
public @interface ValidEan {

    String message() default "Ean code must be numeric and with length of 13";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
