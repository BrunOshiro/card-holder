package com.jazztech.cardholder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.jazztech.cardholder.infrastructure.handler.exception.CardHolderNotFound;
import com.jazztech.cardholder.infrastructure.persistence.mapper.CardHolderMapperImpl;
import com.jazztech.cardholder.infrastructure.persistence.repository.CardHolderRepository;
import com.jazztech.cardholder.service.CardHolderSearch;
import java.util.List;
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
}
