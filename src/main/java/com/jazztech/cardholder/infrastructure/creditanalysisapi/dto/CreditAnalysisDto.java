package com.jazztech.cardholder.infrastructure.creditanalysisapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CreditAnalysisDto(
        UUID id,
        Boolean approved,
        BigDecimal approvedLimit,
        BigDecimal withdraw,
        BigDecimal annualInterest,
        UUID clientId,
        LocalDateTime date
) {
}
