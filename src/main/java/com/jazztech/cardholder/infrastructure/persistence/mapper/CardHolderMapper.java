package com.jazztech.cardholder.infrastructure.persistence.mapper;

import com.jazztech.cardholder.domain.CardHolderDomain;
import com.jazztech.cardholder.infrastructure.persistence.entity.CardHolderEntity;
import com.jazztech.cardholder.presentation.dto.CardHolderResponseDto;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CardHolderMapper {
    @BeforeMapping
    default void generateId(@MappingTarget CardHolderEntity cardHolderEntity) {
        cardHolderEntity.setId(java.util.UUID.randomUUID());
    }

    @Mapping(source = "bankAccount.account", target = "bankAccount")
    @Mapping(source = "bankAccount.agency", target = "bankAgency")
    @Mapping(source = "bankAccount.bankCode", target = "bankCode")
    CardHolderEntity domainToEntity(CardHolderDomain cardHolderDomain);

    @Mapping(source = "id", target = "cardHolderId")
    CardHolderResponseDto entityToDto(CardHolderEntity cardHolderEntity);
}
