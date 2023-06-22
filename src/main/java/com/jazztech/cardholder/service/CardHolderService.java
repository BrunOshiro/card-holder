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
import java.math.RoundingMode;
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
    private static final Integer ROUND = 2;

    private final CreditAnalysisApi creditAnalysisApi;
    private final CardHolderMapper cardHolderMapper;
    private final CardHolderRepository cardHolderRepository;

    @Transactional
    public CardHolderResponseDto createCardHolder(@Valid CardHolderRequestDto cardHolderRequestDto) {
        final CreditAnalysisDto creditAnalysis =
                getCreditAnalysisFromApiAndValidate(cardHolderRequestDto.creditAnalysisId());

        final CardHolderDomain cardHolderDomain = createCardHolderDomain(cardHolderRequestDto, creditAnalysis);
        final CardHolderEntity savedCardHolderEntity = saveCardHolder(cardHolderMapper.domainToEntity(cardHolderDomain));

        LOGGER.info("CardHolder created: {}", savedCardHolderEntity);
        return cardHolderMapper.entityToDto(savedCardHolderEntity);
    }

    private CreditAnalysisDto getCreditAnalysisFromApiAndValidate(UUID creditAnalysisId) {
        final CreditAnalysisDto creditAnalysisDto = creditAnalysisApi.getCreditAnalysisId(creditAnalysisId);

        if (creditAnalysisDto.approved()) {
            return creditAnalysisDto;
        } else {
            throw new CreditAnalysisNotApproved("Credit Analysis " + creditAnalysisId + " not approved");
        }
    }

    private CardHolderDomain createCardHolderDomain(CardHolderRequestDto cardHolderRequestDto, CreditAnalysisDto creditAnalysisDto) {
        return CardHolderDomain.builder()
                .clientId(cardHolderRequestDto.clientId())
                .creditAnalysisId(creditAnalysisDto.id())
                .status(CardHolderStatusEnum.ACTIVE)
                .creditLimit(creditAnalysisDto.approvedLimit().setScale(ROUND, RoundingMode.HALF_UP))
                .bankAccount(createBankAccountDomain(cardHolderRequestDto))
                .createdAt(LocalDateTime.now())
                .build();
    }

    private CardHolderDomain.BankAccountDomain createBankAccountDomain(CardHolderRequestDto cardHolderRequestDto) {
        if (!cardHolderRequestDto.isBankAccountValid()) {
            throw new IllegalArgumentException("Invalid bank account details");
        }
        assert cardHolderRequestDto.bankAccount() != null;

        return CardHolderDomain.BankAccountDomain.builder()
                .account(cardHolderRequestDto.bankAccount().account())
                .agency(cardHolderRequestDto.bankAccount().agency())
                .bankCode(cardHolderRequestDto.bankAccount().bankCode())
                .build();
    }

    private CardHolderEntity saveCardHolder(CardHolderEntity cardHolderEntity) {
        final UUID clientId = cardHolderEntity.getClientId();

        if (cardHolderRepository.findByClientId(clientId) != null) {
            throw new CardHolderAlreadyExists("Client " + clientId + " is already a CardHolder");
        }

        try {
            return cardHolderRepository.save(cardHolderEntity);
        } catch (CardHolderAlreadyExists e) {
            LOGGER.error("Error saving CardHolder: {}", cardHolderEntity, e);
            throw new RuntimeException("Failed to save the Card Holder " + cardHolderEntity);
        }
    }
}
