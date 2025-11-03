package com.acme.ibanvalidator.model;

public record BankDataModel(
        String bankCode,
        String name,
        String zip,
        String city,
        String bic
) {}
