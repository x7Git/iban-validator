package com.acme.ibanvalidator.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.regex.Pattern;

public class IbanValidator implements ConstraintValidator<IbanVal, String> {
    private static final Pattern compile = Pattern.compile("^[A-Za-z]{2}\\s?\\d{2}\\s?([A-Z0-9]\\s?){11,28}[A-Z0-9]$");

    @Override
    public void initialize(IbanVal constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(final String iban, ConstraintValidatorContext context) {
        if(iban == null || iban.isBlank() || !compile.matcher(iban).matches()) {
            return false;
        }

        final String normalized = StringUtils.trimAllWhitespace(iban.toUpperCase());

        char[] countryCode = normalized.substring(0, 2).toCharArray();
        if(!isValidCountryLength(countryCode, iban.length())) {
            return false;
        }

        final int firstLetter = Character.getNumericValue(countryCode[0]);
        final int secondLetter = Character.getNumericValue(countryCode[1]);

        final String rearranged = normalized.substring(4) + firstLetter + secondLetter + normalized.substring(2, 4);

        return calculateMod97(rearranged);
    }

    private static boolean calculateMod97(final CharSequence number) {
        int remainder = 0;

        for (int i = 0; i < number.length(); i++) {
            int digit = number.charAt(i) - '0';
            remainder = (remainder * 10 + digit) % 97;
        }

        return remainder == 1;
    }

    private static boolean isValidCountryLength(char[] countryCode, int length) {
        return switch (Arrays.toString(countryCode)) {
            case "AT" -> length == 20;
            case "CH" -> length == 21;
            case "DE" -> length == 22;
            default -> length >= 15 && length <= 34;
        };
    }
}
