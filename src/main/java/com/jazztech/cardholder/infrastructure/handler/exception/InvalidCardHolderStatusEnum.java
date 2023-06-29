package com.jazztech.cardholder.infrastructure.handler.exception;

public class InvalidCardHolderStatusEnum extends RuntimeException {
    public InvalidCardHolderStatusEnum(String message) {
        super(message);
    }
}
