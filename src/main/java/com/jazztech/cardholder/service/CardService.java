package com.jazztech.cardholder.service;

import com.jazztech.cardholder.domain.CardDomain;
import com.jazztech.cardholder.infrastructure.handler.exception.CardHolderNotFound;
import com.jazztech.cardholder.infrastructure.handler.exception.CreditLimitNotAvailable;
import com.jazztech.cardholder.infrastructure.persistence.entity.CardEntity;
import com.jazztech.cardholder.infrastructure.persistence.entity.CardHolderEntity;
import com.jazztech.cardholder.infrastructure.persistence.mapper.CardMapper;
import com.jazztech.cardholder.infrastructure.persistence.repository.CardHolderRepository;
import com.jazztech.cardholder.infrastructure.persistence.repository.CardRepository;
import com.jazztech.cardholder.presentation.dto.CardResponseDto;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CardService {
    CardHolderRepository cardHolderRepository;
    CardRepository cardRepository;
    CardMapper cardMapper;
    CardDomain cardDomain;

    @Transactional
    public CardResponseDto createCard(UUID cardHolderId, BigDecimal limitRequested) {
        BigDecimal limitAvailable = getCreditAvailableToTheCardHolder(cardHolderId, limitRequested);
        CardDomain createdCardDomain = createCardDomain(cardHolderId, limitRequested, limitAvailable);
        CardEntity savedCardEntity = cardRepository.save(cardMapper.domainToEntity(createdCardDomain));
        return cardMapper.entityToDto(savedCardEntity);
    }

    private BigDecimal getCreditAvailableToTheCardHolder(UUID cardHolderId, BigDecimal creditLimit) {
        if (cardHolderRepository.findById(cardHolderId).isEmpty()) {
            throw new CardHolderNotFound("Card Holder not found with the provided Id: " + cardHolderId);
        }

        CardHolderEntity cardHolderEntity = cardHolderRepository.findById(cardHolderId).get();
        BigDecimal creditLimitApproved = cardHolderEntity.getCreditLimit();
        BigDecimal creditLimitUsed;

        if (cardRepository.findByCardHolderId(cardHolderId) != null) {
            List<CardEntity> cardEntityList = cardRepository.findByCardHolderId(cardHolderId);
            creditLimitUsed = cardEntityList.stream().map(CardEntity::getCreditLimit).reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            creditLimitUsed = BigDecimal.ZERO;
        }

        return cardDomain.limitAvailable(creditLimitApproved, creditLimit, creditLimitUsed);
    }

    private CardDomain createCardDomain(UUID cardHolderId, BigDecimal limitRequested, BigDecimal limitAvailable) {
        if (!cardDomain.isCreditLimitRequestedValid(limitAvailable, limitRequested)) {
            throw new CreditLimitNotAvailable("Credit limit requested is less than the limit available to the card holder.");
        }

        return CardDomain.builder()
                .creditLimit(limitRequested)
                .cardHolderId(cardHolderId)
                .cardNumber(cardDomain.generateCreditCardNumber())
                .cvv(cardDomain.generateCreditCardCvv())
                .dueDate(cardDomain.generateCreditCardDueDate())
                .build();
    }
}
