package com.acme.ibanvalidator.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.util.StringUtils;

public record IbanModel(@NotNull @Pattern(regexp = "^[A-Za-z]{2}\\s?\\d{2}\\s?([A-Z0-9]\\s?){11,28}[A-Z0-9]$", message = "IBAN is not valid") String iban) {

    public IbanModel {
        iban = iban.toUpperCase();
        iban = StringUtils.trimAllWhitespace(iban);
    }
}
