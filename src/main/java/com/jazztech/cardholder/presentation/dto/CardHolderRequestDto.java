package com.jazztech.cardholder.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CardHolderRequestDto(
        UUID clientId,
        UUID creditAnalysisId,
        BankAccount bankAccount
) {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record BankAccount(
            String account,
            String agency,
            String bankCode
    ) {
    }
}
