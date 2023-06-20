package com.jazztech.cardholder.domain;

import com.jazztech.cardholder.infrastructure.creditanalysisapi.dto.CreditAnalysisDto;
import com.jazztech.cardholder.infrastructure.handler.exception.CreditAnalysisNotFound;
import com.jazztech.cardholder.infrastructure.persistence.enums.CardHolderStatusEnum;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
    public static CreditAnalysisDto getCreditAnalysis(List<CreditAnalysisDto> creditAnalysisList, UUID creditAnalysisId) {
        return creditAnalysisList.stream()
                .filter(creditAnalysis -> creditAnalysis.id().equals(creditAnalysisId))
                .findFirst()
                .orElseThrow(() -> new CreditAnalysisNotFound("Credit Analysis " + creditAnalysisId + "not found"));
    }

    @Builder
    public record BankAccountDomain(
            String account,
            String agency,
            String bankCode
    ) {
    }
}
