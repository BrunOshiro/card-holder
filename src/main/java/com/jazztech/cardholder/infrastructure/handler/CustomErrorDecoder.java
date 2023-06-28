package com.jazztech.cardholder.infrastructure.handler;

import com.jazztech.cardholder.infrastructure.handler.exception.CreditAnalysisNotFound;
import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {
    private static final int HTTP_NOT_FOUND = 404;

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == HTTP_NOT_FOUND) {
            return new CreditAnalysisNotFound("Credit Analysis not found for the ID %s");
        }
        return new Default().decode(methodKey, response);
    }

    private String extractClientIdFromUrl(String url) {
        final String[] urlParts = url.split("/");
        return urlParts[urlParts.length - 1];
    }
}
