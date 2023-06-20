package com.jazztech.cardholder.presentation.controller;

import com.jazztech.cardholder.presentation.dto.CardHolderRequestDto;
import com.jazztech.cardholder.presentation.dto.CardHolderResponseDto;
import com.jazztech.cardholder.service.CardHolderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1.0/card-holders")
@RequiredArgsConstructor
@Validated
public class CardHolderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CardHolderController.class);

    private final CardHolderService cardHolderService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CardHolderResponseDto createCardHolder(CardHolderRequestDto cardHolderRequestDto) {
        LOGGER.info("Card Holder creation request: " + cardHolderRequestDto.toString());
        return cardHolderService.createCardHolder(cardHolderRequestDto);
    }
}
