package com.jazztech.cardholder.infrastructure.persistence.mapper;

import com.jazztech.cardholder.domain.entity.Card;
import com.jazztech.cardholder.infrastructure.persistence.entity.CardEntity;
import com.jazztech.cardholder.presentation.dto.CardResponseDto;
import com.jazztech.cardholder.presentation.dto.CardUpdateResponseDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardMapper {
    CardEntity domainToEntity(Card card);

    @Mapping(source = "id", target = "cardId")
    CardResponseDto entityToDto(CardEntity cardEntity);

    @Mapping(source = "id", target = "cardId")
    @Mapping(source = "creditLimit", target = "updatedLimit")
    CardUpdateResponseDto entityToCardUpdateResponseDto(CardEntity cardEntity);

    List<CardResponseDto> entityListToDtoList(List<CardEntity> cardEntityList);
}
