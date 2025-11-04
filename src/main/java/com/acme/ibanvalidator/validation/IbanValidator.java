package com.acme.ibanvalidator.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

public class IbanValidator implements ConstraintValidator<Iban, String> {
    private static final Pattern compile = Pattern.compile("^[A-Za-z]{2}\\s?\\d{2}\\s?([A-Z0-9]\\s?){11,28}[A-Z0-9]$");

    private static boolean calculateMod97(final String numbers) {
        int remainder = 0;

        for (char ch : numbers.toCharArray()) {
            int digit = ch - '0';
            remainder = (remainder * 10 + digit) % 97;
        }

        return remainder == 1;
    }

    private static boolean isValidCountryLength(String countryCode, int length) {
        return switch (countryCode) {
            case "AT" -> length == 20;
            case "CH" -> length == 21;
            case "DE" -> length == 22;
            default -> length >= 15 && length <= 34;
        };
    }

    @Override
    public void initialize(Iban constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(final String iban, ConstraintValidatorContext context) {
        if (iban == null || iban.isBlank() || !compile.matcher(iban).matches()) {
            return false;
        }

        final String normalized = StringUtils.trimAllWhitespace(iban.toUpperCase());

        String countryCode = normalized.substring(0, 2);
        if (!isValidCountryLength(countryCode, normalized.length())) {
            return false;
        }
        char[] countryCodeCharArray = countryCode.toCharArray();

        final int firstLetter = Character.getNumericValue(countryCodeCharArray[0]);
        final int secondLetter = Character.getNumericValue(countryCodeCharArray[1]);

        final String rearranged = normalized.substring(4) + firstLetter + secondLetter + normalized.substring(2, 4);

        return calculateMod97(rearranged);
    }
}
