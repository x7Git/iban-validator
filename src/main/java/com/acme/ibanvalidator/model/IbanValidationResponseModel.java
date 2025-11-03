package com.acme.ibanvalidator.model;

import java.util.List;

public record IbanValidationResponseModel(
        boolean valid,
        List<String> messages,
        String iban,
        BankDataModel bankData,
        CheckResultsModel checkResults
) {}
