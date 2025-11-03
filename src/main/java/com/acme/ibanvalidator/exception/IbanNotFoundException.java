package com.acme.ibanvalidator.exception;

public class IbanNotFoundException extends RuntimeException {

    public IbanNotFoundException(String message) {
        super(message);
    }
}
