package com.jazztech.cardholder.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jazztech.cardholder.infrastructure.persistence.CardHolderStatusEnum;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CardHolderResponseDto(
        UUID cardHolderId,
        CardHolderStatusEnum status,
        BigDecimal limit,
        LocalDateTime createdAt
) {
}
