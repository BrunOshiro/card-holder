package com.jazztech.cardholder.service;

import static com.jazztech.cardholder.domain.CardHolderDomain.getMostRecentCreditAnalysis;

import com.jazztech.cardholder.domain.CardHolderDomain;
import com.jazztech.cardholder.infrastructure.creditanalysisapi.CreditAnalysisApi;
import com.jazztech.cardholder.infrastructure.creditanalysisapi.dto.CreditAnalysisDto;
import com.jazztech.cardholder.infrastructure.persistence.entity.CardHolderEntity;
import com.jazztech.cardholder.infrastructure.persistence.enums.CardHolderStatusEnum;
import com.jazztech.cardholder.infrastructure.persistence.mapper.CardHolderMapper;
import com.jazztech.cardholder.infrastructure.persistence.repository.CardHolderRepository;
import com.jazztech.cardholder.presentation.dto.CardHolderRequestDto;
import com.jazztech.cardholder.presentation.dto.CardHolderResponseDto;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@Validated
@Service
public class CardHolderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CardHolderService.class);

    CreditAnalysisApi creditAnalysisApi;
    CardHolderMapper cardHolderMapper;
    CardHolderRepository cardHolderRepository;

    @Transactional
    public CardHolderResponseDto createCardHoler(CardHolderRequestDto cardHolderRequestDto) {
        final CreditAnalysisDto creditAnalysis = getCreditAnalysis(cardHolderRequestDto.clientId());
        final CardHolderDomain cardHolderDomain = CardHolderDomain.builder()
                .clientId(cardHolderRequestDto.clientId())
                .creditAnalysisId(creditAnalysis.id())
                .status(creditAnalysis.approved()
                        ? CardHolderStatusEnum.ACTIVE
                        : CardHolderStatusEnum.INACTIVE)
                .limit(creditAnalysis.approvedLimit())
                .bankAccount(CardHolderDomain.BankAccountDomain.builder()
                        .account(cardHolderRequestDto.bankAccount().account())
                        .agency(cardHolderRequestDto.bankAccount().agency())
                        .bankCode(cardHolderRequestDto.bankAccount().bankCode())
                        .build())
                .createdAt(LocalDateTime.now())
                .build();

        final CardHolderEntity cardHolderEntity = cardHolderMapper.domainToEntity(cardHolderDomain);
        final CardHolderEntity savedCardHolderEntity = cardHolderRepository.save(cardHolderEntity);

        LOGGER.info("CardHolder created: {}", savedCardHolderEntity);
        return cardHolderMapper.entityToDto(savedCardHolderEntity);
    }

    private CreditAnalysisDto getCreditAnalysis(UUID clientId) {
        final List<CreditAnalysisDto> creditAnalysisByClientId = creditAnalysisApi.getCreditAnalysisByClientId(clientId);
        return getMostRecentCreditAnalysis(creditAnalysisByClientId);
    }
}
