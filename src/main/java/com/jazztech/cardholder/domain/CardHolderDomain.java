package com.jazztech.cardholder.domain;

import com.jazztech.cardholder.infrastructure.persistence.enums.CardHolderStatusEnum;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record CardHolderDomain(
        UUID clientId,
        UUID creditAnalysisId,
        CardHolderStatusEnum status,
        BigDecimal creditLimit,
        BankAccountDomain bankAccount,
        LocalDateTime createdAt
) {
    @Builder
    public record BankAccountDomain(
            String account,
            String agency,
            String bankCode
    ) {
    }
}
