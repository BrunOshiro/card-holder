package com.jazztech.cardholder.service;

import com.jazztech.cardholder.infrastructure.handler.exception.CardHolderNotFound;
import com.jazztech.cardholder.infrastructure.persistence.enums.CardHolderStatusEnum;
import com.jazztech.cardholder.infrastructure.persistence.mapper.CardHolderMapper;
import com.jazztech.cardholder.infrastructure.persistence.repository.CardHolderRepository;
import com.jazztech.cardholder.presentation.dto.CardHolderResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CardHolderSearch {
    private final CardHolderRepository cardHolderRepository;
    private final CardHolderMapper cardHolderMapper;

    public List<CardHolderResponseDto> getAllByStatus(CardHolderStatusEnum statusEnum) {
        return switch (statusEnum) {
        case ACTIVE -> cardHolderMapper.entityListToDtoList(
                    cardHolderRepository.findByStatusEquals(CardHolderStatusEnum.ACTIVE));
        case INACTIVE -> cardHolderMapper.entityListToDtoList(
                    cardHolderRepository.findByStatusEquals(CardHolderStatusEnum.INACTIVE));
        default -> throw new CardHolderNotFound("Card Holder not found with the parameters provided. Status: " + statusEnum);
        };
    }

    public List<CardHolderResponseDto> getAll() {
        try {
            return cardHolderMapper.entityListToDtoList(cardHolderRepository.findAll());
        } catch (CardHolderNotFound e) {
            throw new CardHolderNotFound("There is no Card Holder registered yet");
        }
    }
}
