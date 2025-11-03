package com.acme.ibanvalidator.ibanvalidator.connector;

import com.acme.ibanvalidator.model.IbanModel;
import com.acme.ibanvalidator.model.IbanValidationResponseModel;
import org.jspecify.annotations.NonNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class IbanConnector {
    private final RestClient ibanRestClient;

    public IbanConnector(RestClient ibanRestClient) {
        this.ibanRestClient = ibanRestClient;
    }

    public IbanValidationResponseModel checkIBAN(IbanModel iban){
        ResponseEntity<@NonNull IbanValidationResponseModel> response = ibanRestClient.get().uri("/validate/%s?getBIC=true&validateBankCode=true".formatted(iban.iban())).accept(MediaType.APPLICATION_JSON).retrieve().toEntity(IbanValidationResponseModel.class);
        return response.getBody();
    }
}
