package com.jazztech.cardholder.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CardResponseDto(
        UUID cardId,
        String cardNumber,
        String cvv,
        LocalDate dueDate
) {
}
