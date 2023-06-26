package com.jazztech.cardholder.infrastructure.handler.exception;

public class CardHolderAlreadyExists extends RuntimeException {
    public CardHolderAlreadyExists(String message) {
        super(message);
    }
}
