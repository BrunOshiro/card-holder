package com.jazztech.cardholder.infrastructure.persistence.mapper;

import com.jazztech.cardholder.domain.CardDomain;
import com.jazztech.cardholder.infrastructure.persistence.entity.CardEntity;
import com.jazztech.cardholder.presentation.dto.CardResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardMapper {
    CardEntity domainToEntity(CardDomain cardDomain);

    @Mapping(source = "id", target = "cardId")
    CardResponseDto entityToDto(CardEntity cardEntity);
}
