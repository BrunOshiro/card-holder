package com.jazztech.cardholder.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record CardUpdateResponseDto(
        UUID cardId,
        BigDecimal updatedLimit
) {
}
