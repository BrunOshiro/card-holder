package com.jazztech.cardholder.service;

import com.jazztech.cardholder.domain.CardHolderDomain;
import com.jazztech.cardholder.infrastructure.creditanalysisapi.CreditAnalysisApi;
import com.jazztech.cardholder.infrastructure.creditanalysisapi.dto.CreditAnalysisDto;
import com.jazztech.cardholder.infrastructure.handler.exception.CardHolderAlreadyExists;
import com.jazztech.cardholder.infrastructure.handler.exception.CreditAnalysisNotApproved;
import com.jazztech.cardholder.infrastructure.persistence.entity.CardHolderEntity;
import com.jazztech.cardholder.infrastructure.persistence.enums.CardHolderStatusEnum;
import com.jazztech.cardholder.infrastructure.persistence.mapper.CardHolderMapper;
import com.jazztech.cardholder.infrastructure.persistence.repository.CardHolderRepository;
import com.jazztech.cardholder.presentation.dto.CardHolderRequestDto;
import com.jazztech.cardholder.presentation.dto.CardHolderResponseDto;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
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

    private final CreditAnalysisApi creditAnalysisApi;
    private final CardHolderMapper cardHolderMapper;
    private final CardHolderRepository cardHolderRepository;

    @Transactional
    public CardHolderResponseDto createCardHolder(@Valid CardHolderRequestDto cardHolderRequestDto) {
        final CreditAnalysisDto creditAnalysis =
                getCreditAnalysisFromCreditAnalysisApi(cardHolderRequestDto.creditAnalysisId());
        final CardHolderDomain cardHolderDomain = CardHolderDomain.builder()
                .clientId(cardHolderRequestDto.clientId())
                .creditAnalysisId(creditAnalysis.id())
                .status(CardHolderStatusEnum.ACTIVE)
                .creditLimit(creditAnalysis.approvedLimit())
                .bankAccount(CardHolderDomain.BankAccountDomain.builder()
                        .account(cardHolderRequestDto.bankAccount().account())
                        .agency(cardHolderRequestDto.bankAccount().agency())
                        .bankCode(cardHolderRequestDto.bankAccount().bankCode())
                        .build())
                .createdAt(LocalDateTime.now())
                .build();

        final CardHolderEntity cardHolderEntity = saveCardHolder(cardHolderMapper.domainToEntity(cardHolderDomain));
        final CardHolderEntity savedCardHolderEntity = cardHolderRepository.save(cardHolderEntity);

        LOGGER.info("CardHolder created: {}", savedCardHolderEntity);
        return cardHolderMapper.entityToDto(savedCardHolderEntity);
    }

    private CreditAnalysisDto getCreditAnalysisFromCreditAnalysisApi(UUID creditAnalysisId) {
        final CreditAnalysisDto creditAnalysisDto = creditAnalysisApi.getCreditAnalysisId(creditAnalysisId);

        if (creditAnalysisDto.approved()) {
            return creditAnalysisDto;
        } else {
            throw new CreditAnalysisNotApproved("Credit Analysis " + creditAnalysisId + " not approved");
        }
    }

    private CardHolderEntity saveCardHolder(CardHolderEntity cardHolderEntity) {
        try {
            return cardHolderRepository.save(cardHolderEntity);
        } catch (CardHolderAlreadyExists e) {
            LOGGER.error("Error saving CardHolder: {}", cardHolderEntity, e);
            throw new CardHolderAlreadyExists("CardHolder " + cardHolderEntity + " already exists");
        }
    }
}
