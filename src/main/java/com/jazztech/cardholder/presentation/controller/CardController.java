package com.jazztech.cardholder.presentation.controller;

import com.jazztech.cardholder.presentation.dto.CardResponseDto;
import com.jazztech.cardholder.service.CardService;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1.0/card-holders")
@RequiredArgsConstructor
public class CardController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CardController.class);

    CardService cardService;

    @PostMapping("/{cardHolder}/cards")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CardResponseDto createCard(@PathVariable(value = "cardHolder") UUID cardHolderId,
                                      @RequestBody BigDecimal limit) {
        LOGGER.info("Card creation requested");
        return cardService.createCard(cardHolderId, limit);
    }
}
