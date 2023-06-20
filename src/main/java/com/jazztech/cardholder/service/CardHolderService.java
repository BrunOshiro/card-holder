package com.jazztech.cardholder.service;

import static com.jazztech.cardholder.domain.CardHolderDomain.getMostRecentCreditAnalysis;

import com.jazztech.cardholder.infrastructure.creditanalysisapi.CreditAnalysisApi;
import com.jazztech.cardholder.infrastructure.creditanalysisapi.dto.CreditAnalysisDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Validated
@Service
public class CardHolderService {
    CreditAnalysisApi creditAnalysisApi;



    private CreditAnalysisDto getCreditAnalysis(UUID clientId) {
        final List<CreditAnalysisDto> creditAnalysisByClientId = creditAnalysisApi.getCreditAnalysisByClientId(clientId);
        return getMostRecentCreditAnalysis(creditAnalysisByClientId);
    }
}
