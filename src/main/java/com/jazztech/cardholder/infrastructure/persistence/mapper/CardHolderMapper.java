package com.jazztech.cardholder.infrastructure.persistence.mapper;

import com.jazztech.cardholder.domain.CardHolderDomain;
import com.jazztech.cardholder.infrastructure.persistence.entity.CardHolderEntity;
import com.jazztech.cardholder.presentation.dto.CardHolderResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardHolderMapper {
    CardHolderEntity domainToEntity(CardHolderDomain cardHolderDomain);

    @Mapping(source = "id", target = "cardHolderId")
    CardHolderResponseDto entityToDto(CardHolderEntity cardHolderEntity);
}
