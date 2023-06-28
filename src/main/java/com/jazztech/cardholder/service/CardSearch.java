package com.jazztech.cardholder.service;

import com.jazztech.cardholder.infrastructure.handler.exception.CardNotFound;
import com.jazztech.cardholder.infrastructure.persistence.mapper.CardMapper;
import com.jazztech.cardholder.infrastructure.persistence.repository.CardRepository;
import com.jazztech.cardholder.presentation.dto.CardResponseDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CardSearch {
    private static final Logger LOGGER = LoggerFactory.getLogger(CardSearch.class);
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    public List<CardResponseDto> getAllByCardHolder(UUID cardHolderId) {
        List<CardResponseDto> cardsListByCardHolder;

        try {
            cardsListByCardHolder = cardMapper.entityListToDtoList(cardRepository.findByCardHolderId(cardHolderId));
        } catch (CardNotFound e) {
            throw new CardNotFound("Card not found to the Card Holder Id: " + cardHolderId);
        }

        LOGGER.info("Card found to the Card Holder Id: " + cardHolderId);
        return cardsListByCardHolder;
    }
}
