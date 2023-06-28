package com.jazztech.cardholder.domain.entity;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record Card(
        UUID cardHolderId,
        BigDecimal creditLimit,
        String cardNumber,
        String cvv,
        String dueDate
) {
}
