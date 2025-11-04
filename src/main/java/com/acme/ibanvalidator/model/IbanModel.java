package com.acme.ibanvalidator.model;

import com.acme.ibanvalidator.validation.Iban;

public record IbanModel(@Iban String iban) { }
