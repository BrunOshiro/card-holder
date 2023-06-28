package com.jazztech.cardholder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.wildfly.common.Assert.assertNotNull;

import com.jazztech.cardholder.infrastructure.handler.exception.CardNotFound;
import com.jazztech.cardholder.infrastructure.persistence.entity.CardEntity;
import com.jazztech.cardholder.infrastructure.persistence.mapper.CardMapperImpl;
import com.jazztech.cardholder.infrastructure.persistence.repository.CardRepository;
import com.jazztech.cardholder.presentation.dto.CardResponseDto;
import com.jazztech.cardholder.service.CardSearch;
import com.jazztech.infrasctructure.Factory;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CardSearchTest {
    @InjectMocks
    CardSearch cardSearch;

    @Mock
    private CardRepository cardRepository;

    @Spy
    private CardMapperImpl mapperImpl;

    @Test
    public void should_throw_exception_when_card_not_found_to_the_holder() {
        when(cardRepository.findByCardHolderId(UUID.fromString("5619f798-d929-47ab-9b9d-4126905c2e11"))).thenReturn(List.of());

        assertThrows(CardNotFound.class, () -> cardSearch.getAllByCardHolder(UUID.fromString("5619f798-d929-47ab-9b9d-4126905c2e11")));
    }

    @Test
    public void should_get_all_holder_cards() {
        List<CardEntity> cardEntityList = Factory.cardEntityListFactory(4);
        when(cardRepository.findByCardHolderId(UUID.fromString("5619f798-d929-47ab-9b9d-4126905c2e11"))).thenReturn(cardEntityList);

        List<CardResponseDto> cardResponseDtoList =  cardSearch.getAllByCardHolder(UUID.fromString("5619f798-d929-47ab-9b9d-4126905c2e11"));

        assertNotNull(cardResponseDtoList);
        assertEquals(cardEntityList.size(), cardResponseDtoList.size());
    }
}
