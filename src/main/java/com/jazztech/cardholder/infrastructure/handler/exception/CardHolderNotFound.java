package com.jazztech.cardholder.infrastructure.handler.exception;

public class CardHolderNotFound extends RuntimeException {
    public CardHolderNotFound(String message) {
        super(message);
    }
}