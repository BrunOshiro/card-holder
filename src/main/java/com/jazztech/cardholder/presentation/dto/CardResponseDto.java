package com.jazztech.cardholder.presentation.dto;

import java.time.LocalDate;
import java.util.UUID;

public record CardResponseDto(
        UUID cardId,
        String cardNumber,
        String cvv,
        LocalDate dueDate
) {
}
