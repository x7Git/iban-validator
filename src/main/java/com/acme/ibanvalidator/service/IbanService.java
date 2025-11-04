package com.acme.ibanvalidator.service;

import com.acme.ibanvalidator.connector.IbanConnector;
import com.acme.ibanvalidator.exception.IbanNotFoundException;
import com.acme.ibanvalidator.model.IbanModel;
import com.acme.ibanvalidator.model.IbanValidationResponseModel;
import com.acme.ibanvalidator.model.dto.BankDataModelDto;
import org.springframework.stereotype.Service;

@Service
public class IbanService {

    private final IbanConnector ibanConnector;

    public IbanService(IbanConnector ibanConnector) {
        this.ibanConnector = ibanConnector;
    }

    public BankDataModelDto validateIBAN(IbanModel iban){
        IbanValidationResponseModel ibanValidationResponseModel = ibanConnector.checkIBAN(iban);
        if(ibanValidationResponseModel.valid()){
            return new BankDataModelDto(ibanValidationResponseModel.bankData().name(), ibanValidationResponseModel.bankData().bic());
        }
        throw new IbanNotFoundException("This Iban '%s' seems not to be valid".formatted(ibanValidationResponseModel.iban()));
    }
}
