package com.acme.ibanvalidator.model;

import com.acme.ibanvalidator.validation.IbanVal;

public record IbanModel(@IbanVal String iban) { }
