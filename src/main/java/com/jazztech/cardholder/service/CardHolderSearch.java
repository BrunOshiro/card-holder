package com.jazztech.cardholder.service;

import com.jazztech.cardholder.infrastructure.handler.exception.CardHolderNotFound;
import com.jazztech.cardholder.infrastructure.handler.exception.InvalidCardHolderStatusEnum;
import com.jazztech.cardholder.infrastructure.persistence.enums.CardHolderStatusEnum;
import com.jazztech.cardholder.infrastructure.persistence.mapper.CardHolderMapper;
import com.jazztech.cardholder.infrastructure.persistence.repository.CardHolderRepository;
import com.jazztech.cardholder.presentation.dto.CardHolderResponseDto;
import java.util.EnumSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CardHolderSearch {
    private final CardHolderRepository cardHolderRepository;
    private final CardHolderMapper cardHolderMapper;

    public List<CardHolderResponseDto> getAllByStatus(String status) {
        CardHolderStatusEnum statusEnum;

        try {
            statusEnum = CardHolderStatusEnum.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCardHolderStatusEnum(
                    "Card Holder not found with the parameters provided. Status: " + status
                            + ". The expected parameters are one of these: " + EnumSet.allOf(CardHolderStatusEnum.class));
        }

        if (cardHolderRepository.findByStatusEquals(statusEnum).isEmpty()) {
            throw new CardHolderNotFound("Card Holder not found with the parameters provided. Status: " + statusEnum);
        }
        return switch (statusEnum) {
            case ACTIVE -> cardHolderMapper.entityListToDtoList(
                    cardHolderRepository.findByStatusEquals(CardHolderStatusEnum.ACTIVE));
            case INACTIVE -> cardHolderMapper.entityListToDtoList(
                    cardHolderRepository.findByStatusEquals(CardHolderStatusEnum.INACTIVE));
        };
    }

    public List<CardHolderResponseDto> getAll() {
        if (cardHolderRepository.findAll().isEmpty()) {
            throw new CardHolderNotFound("There is no Card Holder registered yet");
        }
        return cardHolderMapper.entityListToDtoList(cardHolderRepository.findAll());
    }
}
