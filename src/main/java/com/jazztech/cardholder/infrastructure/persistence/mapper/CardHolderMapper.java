package com.jazztech.cardholder.infrastructure.persistence.mapper;

import com.jazztech.cardholder.domain.entity.CardHolder;
import com.jazztech.cardholder.infrastructure.persistence.entity.CardHolderEntity;
import com.jazztech.cardholder.presentation.dto.CardHolderResponseDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardHolderMapper {
    CardHolderEntity domainToEntity(CardHolder cardHolder);

    @Mapping(source = "id", target = "cardHolderId")
    CardHolderResponseDto entityToDto(CardHolderEntity cardHolderEntity);

    List<CardHolderResponseDto> entityListToDtoList(List<CardHolderEntity> cardHolderEntity);
}
