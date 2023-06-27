package com.jazztech.cardholder.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CardResponseDto(
        UUID cardId,
        String cardNumber,
        String cvv,
        String dueDate
) {
}
