package com.jazztech.cardholder.service;

import com.jazztech.cardholder.domain.CardDomain;
import com.jazztech.cardholder.infrastructure.handler.exception.CardHolderNotFound;
import com.jazztech.cardholder.infrastructure.persistence.entity.CardEntity;
import com.jazztech.cardholder.infrastructure.persistence.mapper.CardMapper;
import com.jazztech.cardholder.infrastructure.persistence.repository.CardHolderRepository;
import com.jazztech.cardholder.infrastructure.persistence.repository.CardRepository;
import com.jazztech.cardholder.presentation.dto.CardResponseDto;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class CardService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CardService.class);

    private final CardHolderRepository cardHolderRepository;
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final CardDomain cardDomain;

    @Transactional
    public CardResponseDto createCard(UUID cardHolderId, BigDecimal limitRequested) {
        final BigDecimal limitAvailable = getCreditAvailableToTheCardHolder(cardHolderId, limitRequested);
        final CardDomain createdCardDomain = cardDomain.createCardDomain(cardHolderId, limitRequested, limitAvailable);
        final CardEntity savedCardEntity = cardRepository.save(cardMapper.domainToEntity(createdCardDomain));
        LOGGER.info("Card created: {}", savedCardEntity);
        return cardMapper.entityToDto(savedCardEntity);
    }

    private BigDecimal getCreditAvailableToTheCardHolder(UUID cardHolderId, BigDecimal limitRequested) {
        if (cardHolderRepository.findById(cardHolderId).isEmpty()) {
            throw new CardHolderNotFound("Card Holder not found with the provided Id: " + cardHolderId);
        }

        final BigDecimal creditLimitApproved = cardHolderRepository.findById(cardHolderId).get().getCreditLimit();
        final BigDecimal creditLimitUsed;

        if (cardRepository.findByCardHolderId(cardHolderId) != null) {
            final List<CardEntity> cardEntityList = cardRepository.findByCardHolderId(cardHolderId);
            creditLimitUsed = cardEntityList.stream().map(CardEntity::getCreditLimit).reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            creditLimitUsed = BigDecimal.ZERO;
        }

        return cardDomain.limitAvailable(creditLimitApproved, limitRequested, creditLimitUsed);
    }
}
