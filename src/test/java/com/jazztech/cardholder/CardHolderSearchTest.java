package com.jazztech.cardholder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.jazztech.cardholder.infrastructure.handler.exception.CardHolderNotFound;
import com.jazztech.cardholder.infrastructure.handler.exception.InvalidCardHolderStatusEnum;
import com.jazztech.cardholder.infrastructure.persistence.entity.CardHolderEntity;
import com.jazztech.cardholder.infrastructure.persistence.enums.CardHolderStatusEnum;
import com.jazztech.cardholder.infrastructure.persistence.mapper.CardHolderMapperImpl;
import com.jazztech.cardholder.infrastructure.persistence.repository.CardHolderRepository;
import com.jazztech.cardholder.service.CardHolderSearch;
import com.jazztech.infrasctructure.Factory;
import java.util.List;
import java.util.stream.Collectors;
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
public class CardHolderSearchTest {
    @InjectMocks
    CardHolderSearch cardHolderSearch;

    @Mock
    private CardHolderRepository cardHolderRepository;

    @Spy
    private CardHolderMapperImpl mapperImpl;

    @Test
    public void should_throw_exception_when_not_find_card_holders() {
        when(cardHolderRepository.findAll()).thenReturn(List.of());

        assertThrows(CardHolderNotFound.class, () -> cardHolderSearch.getAll());
    }

    @Test
    public void should_throw_exception_when_card_holders_not_find_by_status() {
        when(cardHolderRepository.findByStatusEquals(CardHolderStatusEnum.ACTIVE)).thenReturn(List.of());

        assertThrows(CardHolderNotFound.class, () -> cardHolderSearch.getAllByStatus("ACTIVE"));
    }

    @Test
    public void should_throw_exception_when_card_holder_status_enum_is_not_valid() {
        assertThrows(InvalidCardHolderStatusEnum.class, () -> cardHolderSearch.getAllByStatus("NOT_LISTED_STATUS"));
    }

    @Test
    public void should_get_all_card_holders_by_status_enum_active_param_using_lower_case() {
        List<CardHolderEntity> cardHolderEntities = Factory.cardHolderEntityListFactory(5);

        when(cardHolderRepository.findByStatusEquals(CardHolderStatusEnum.ACTIVE)).thenReturn(cardHolderEntities.stream()
                .filter(cardHolderEntity -> cardHolderEntity.getStatus().equals(CardHolderStatusEnum.ACTIVE))
                .collect(Collectors.toList()));

        cardHolderSearch.getAllByStatus("active");

        assertEquals(cardHolderEntities.stream()
                .filter(cardHolderEntity -> cardHolderEntity.getStatus().equals(CardHolderStatusEnum.ACTIVE))
                .count(), cardHolderSearch.getAllByStatus("active").size());
    }

    @Test
    public void should_get_all_card_holders_by_status_enum_inactive() {
        List<CardHolderEntity> cardHolderEntities = Factory.cardHolderEntityListFactory(10);

        when(cardHolderRepository.findByStatusEquals(CardHolderStatusEnum.INACTIVE)).thenReturn(cardHolderEntities.stream()
                .filter(cardHolderEntity -> cardHolderEntity.getStatus().equals(CardHolderStatusEnum.INACTIVE))
                .collect(Collectors.toList()));

        cardHolderSearch.getAllByStatus("INACTIVE");

        assertEquals(cardHolderEntities.stream()
                .filter(cardHolderEntity -> cardHolderEntity.getStatus().equals(CardHolderStatusEnum.INACTIVE))
                .count(), cardHolderSearch.getAllByStatus("INACTIVE").size());
    }

    @Test
    public void should_get_all_card_holders_by_status_enum_active() {
        List<CardHolderEntity> cardHolderEntities = Factory.cardHolderEntityListFactory(15);

        when(cardHolderRepository.findByStatusEquals(CardHolderStatusEnum.ACTIVE)).thenReturn(cardHolderEntities.stream()
                .filter(cardHolderEntity -> cardHolderEntity.getStatus().equals(CardHolderStatusEnum.ACTIVE))
                .collect(Collectors.toList()));

        cardHolderSearch.getAllByStatus("ACTIVE");

        assertEquals(cardHolderEntities.stream()
                .filter(cardHolderEntity -> cardHolderEntity.getStatus().equals(CardHolderStatusEnum.ACTIVE))
                .count(), cardHolderSearch.getAllByStatus("ACTIVE").size());
    }

    @Test
    public void should_get_all_card_holders() {
        List<CardHolderEntity> cardHolderEntities = Factory.cardHolderEntityListFactory(30);

        when(cardHolderRepository.findAll()).thenReturn(cardHolderEntities);

        cardHolderSearch.getAll();

        assertEquals(cardHolderEntities.size(), cardHolderSearch.getAll().size());
    }
}
