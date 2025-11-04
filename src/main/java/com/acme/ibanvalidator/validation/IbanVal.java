package com.acme.ibanvalidator.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = IbanValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IbanVal {
    String message() default "IBAN is not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
