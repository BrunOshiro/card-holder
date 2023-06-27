package com.jazztech.cardholder.infrastructure.handler.exception;

public class CreditLimitNotAvailable extends RuntimeException {
    public CreditLimitNotAvailable(String message) {
        super(message);
    }
}
