package com.jazztech.cardholder.infrastructure.handler.exception;

public class CardNotFound extends RuntimeException {
    public CardNotFound(String message) {
        super(message);
    }
}
