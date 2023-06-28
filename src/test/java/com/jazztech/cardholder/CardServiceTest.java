package com.jazztech.cardholder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.wildfly.common.Assert.assertNotNull;

import com.jazztech.cardholder.infrastructure.handler.exception.CardHolderInactive;
import com.jazztech.cardholder.infrastructure.handler.exception.CardHolderNotFound;
import com.jazztech.cardholder.infrastructure.handler.exception.CreditLimitNotAvailable;
import com.jazztech.cardholder.infrastructure.persistence.entity.CardEntity;
import com.jazztech.cardholder.infrastructure.persistence.enums.CardHolderStatusEnum;
import com.jazztech.cardholder.infrastructure.persistence.mapper.CardMapperImpl;
import com.jazztech.cardholder.infrastructure.persistence.repository.CardHolderRepository;
import com.jazztech.cardholder.infrastructure.persistence.repository.CardRepository;
import com.jazztech.cardholder.presentation.dto.CardResponseDto;
import com.jazztech.cardholder.service.CardService;
import com.jazztech.infrasctructure.Factory;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class CardServiceTest {
    @InjectMocks
    CardService cardService;

    @Mock
    private CardRepository cardRepository;
    @Mock
    private CardHolderRepository cardHolderRepository;


    @Spy
    private CardMapperImpl mapperImpl;

    @Captor
    private ArgumentCaptor<UUID> cardHolderIdCaptor;
    @Captor
    private ArgumentCaptor<CardEntity> cardEntityCaptor;

    @Test
    public void should_create_card() {
        when(cardHolderRepository.findById(cardHolderIdCaptor.capture()))
                .thenReturn(Optional.ofNullable(Factory.cardHolderEntityFactory()));
        when(cardRepository.save(cardEntityCaptor.capture()))
                .thenReturn(Factory.cardEntityFactory());

        CardResponseDto cardResponseDto =
                cardService.createCard(UUID.fromString("abf5d31a-ebb3-47e0-a441-f0487dd804e9"), BigDecimal.valueOf(3000.00));

        assertNotNull(cardResponseDto);
        assertEquals(cardResponseDto, Factory.cardResponseDtoFactory());
    }

    @Test
    public void should_throw_exception_when_credit_limit_available_is_less_than_requested() {
        when(cardHolderRepository.findById(cardHolderIdCaptor.capture()))
                .thenReturn(Optional.ofNullable(Factory.cardHolderEntityFactory()));

        Exception exception = assertThrows(
                CreditLimitNotAvailable.class,
                () -> cardService.createCard(UUID.fromString("abf5d31a-ebb3-47e0-a441-f0487dd804e9"), BigDecimal.valueOf(5000.00))
        );

        assertEquals("Credit limit requested is less than the limit available to the card holder.", exception.getMessage());
    }

    @Test
    public void should_throw_exception_when_card_holder_not_found() {
        when(cardHolderRepository.findById(cardHolderIdCaptor.capture()))
                .thenReturn(Optional.empty());

        assertThrows(CardHolderNotFound.class,
                () -> cardService.createCard(UUID.fromString("abf5d31a-ebb3-47e0-a441-f0487dd804e9"), BigDecimal.valueOf(3000.00)));
    }

    @Test
    public void should_throw_exception_when_card_holder_is_inactive() {
        when(cardHolderRepository.findById(cardHolderIdCaptor.capture()))
                .thenReturn(Optional.ofNullable(Factory.cardHolderEntityFactory().toBuilder()
                        .status(CardHolderStatusEnum.INACTIVE)
                        .build()));

        assertThrows(CardHolderInactive.class,
                () -> cardService.createCard(UUID.fromString("abf5d31a-ebb3-47e0-a441-f0487dd804e9"), BigDecimal.valueOf(3000.00)));
    }
}
