package com.jazztech.cardholder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.jazztech.cardholder.infrastructure.creditanalysisapi.CreditAnalysisApi;
import com.jazztech.cardholder.infrastructure.creditanalysisapi.dto.CreditAnalysisDto;
import com.jazztech.cardholder.infrastructure.persistence.entity.CardHolderEntity;
import com.jazztech.cardholder.infrastructure.persistence.enums.CardHolderStatusEnum;
import com.jazztech.cardholder.infrastructure.persistence.mapper.CardHolderMapperImpl;
import com.jazztech.cardholder.infrastructure.persistence.repository.CardHolderRepository;
import com.jazztech.cardholder.presentation.dto.CardHolderRequestDto;
import com.jazztech.cardholder.presentation.dto.CardHolderResponseDto;
import com.jazztech.cardholder.service.CardHolderService;
import com.jazztech.infrasctructure.Factory;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
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
public class CardHolderServiceTest {
    private static final Integer ROUND = 2;

    @InjectMocks
    CardHolderService cardHolderService;

    @Mock
    private CreditAnalysisApi creditAnalysisApi;
    @Mock
    private CardHolderRepository cardHolderRepository;

    @Spy
    private CardHolderMapperImpl mapperImpl;

    @Captor
    private ArgumentCaptor<UUID> clientIdCaptor;
    @Captor
    private ArgumentCaptor<CardHolderEntity> cardHolderEntityCaptor;

    @Test
    public void should_create_card_holder_active() {
        when(creditAnalysisApi.getCreditAnalysisId(clientIdCaptor.capture()))
                .thenReturn(Factory.creditAnalysisDtoFactory());
        when(cardHolderRepository.save(cardHolderEntityCaptor.capture()))
                .thenReturn(Factory.cardHolderEntityFactory());

        CardHolderRequestDto cardHolderRequestDto = Factory.cardHolderRequestDtoFactory();

        CardHolderResponseDto cardHolderResponseDto = cardHolderService.createCardHolder(cardHolderRequestDto);

        assertNotNull(cardHolderResponseDto);
        assertEquals(CardHolderStatusEnum.ACTIVE, cardHolderResponseDto.status());
        assertEquals(cardHolderEntityCaptor.getValue().getCreditLimit().setScale(ROUND, RoundingMode.HALF_UP), cardHolderResponseDto.creditLimit());
    }

    @Test
    public void should_create_card_holder_inactive() {
        CreditAnalysisDto creditAnalysisDto = Factory.creditAnalysisDtoFactory().toBuilder()
                .id(UUID.fromString("c85bc938-ecb2-4e33-a442-43fff8bfe9ff"))
                .approved(false)
                .withdraw(BigDecimal.valueOf(0.00))
                .approvedLimit(BigDecimal.valueOf(0.00))
                .annualInterest(BigDecimal.valueOf(0.00))
                .date(LocalDateTime.parse("2023-05-30T14:27:00.408202"))
                .build();
        CardHolderEntity cardHolderEntity = Factory.cardHolderEntityFactory().toBuilder()
                .creditAnalysisId(UUID.fromString("c85bc938-ecb2-4e33-a442-43fff8bfe9ff"))
                .status(CardHolderStatusEnum.INACTIVE)
                .creditLimit(BigDecimal.valueOf(0.00))
                .build();

        when(creditAnalysisApi.getCreditAnalysisId(clientIdCaptor.capture()))
                .thenReturn(creditAnalysisDto);
        when(cardHolderRepository.save(cardHolderEntityCaptor.capture()))
                .thenReturn(cardHolderEntity);

        CardHolderRequestDto cardHolderRequestDto = Factory.cardHolderRequestDtoFactory().toBuilder()
                .creditAnalysisId(UUID.fromString("c85bc938-ecb2-4e33-a442-43fff8bfe9ff"))
                .build();

        CardHolderResponseDto cardHolderResponseDto = cardHolderService.createCardHolder(cardHolderRequestDto);

        assertNotNull(cardHolderResponseDto);
        assertEquals(CardHolderStatusEnum.INACTIVE, cardHolderResponseDto.status());
        assertEquals(cardHolderEntityCaptor.getValue().getCreditLimit().setScale(ROUND, RoundingMode.HALF_UP), cardHolderResponseDto.creditLimit());
    }
}
