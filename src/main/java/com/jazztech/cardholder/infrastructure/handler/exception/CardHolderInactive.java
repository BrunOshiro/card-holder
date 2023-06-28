package com.jazztech.cardholder.infrastructure.handler.exception;

public class CardHolderInactive extends RuntimeException {
    public CardHolderInactive(String message) {
        super(message);
    }
}
