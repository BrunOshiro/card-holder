package com.jazztech.cardholder.domain;

import com.jazztech.cardholder.infrastructure.creditanalysisapi.dto.CreditAnalysisDto;
import com.jazztech.cardholder.infrastructure.persistence.enums.CardHolderStatusEnum;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record CardHolderDomain(
        UUID clientId,
        UUID creditAnalysisId,
        CardHolderStatusEnum status,
        BigDecimal limit,
        BankAccountDomain bankAccount,
        LocalDateTime createdAt
) {
    public static CreditAnalysisDto getMostRecentCreditAnalysis(List<CreditAnalysisDto> creditAnalysisList) {
        return creditAnalysisList.stream()
                .max(Comparator.comparing(CreditAnalysisDto::date))
                .orElse(null);
    }

    @Builder
    public record BankAccountDomain(
            String account,
            String agency,
            String bankCode
    ) {
    }
}
