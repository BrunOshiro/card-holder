package com.jazztech.cardholder.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CardHolderRequestDto(
        UUID clientId,
        UUID creditAnalysisId,
        BankAccount bankAccount
) {
    @Builder(toBuilder = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record BankAccount(
            String account,
            String agency,
            String bankCode
    ) {
    }
}
