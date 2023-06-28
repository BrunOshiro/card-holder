package com.jazztech.cardholder.infrastructure.handler.exception;

public class CreditAnalysisNotApproved extends RuntimeException {
    public CreditAnalysisNotApproved(String message) {
        super(message);
    }
}
