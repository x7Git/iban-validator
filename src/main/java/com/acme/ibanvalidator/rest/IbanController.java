package com.acme.ibanvalidator.rest;

import com.acme.ibanvalidator.model.IbanModel;
import com.acme.ibanvalidator.model.dto.BankDataModelDto;
import com.acme.ibanvalidator.service.IbanService;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class IbanController {

    private final IbanService ibanService;

    public IbanController(IbanService ibanService) {
        this.ibanService = ibanService;
    }

    @PostMapping(path = "/validate-iban", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<@NonNull BankDataModelDto> createIBAN(@RequestBody @Valid IbanModel iban){
        BankDataModelDto bankDataModelDto = ibanService.validateIBAN(iban);
        return ResponseEntity.ok(bankDataModelDto);
    }
}
