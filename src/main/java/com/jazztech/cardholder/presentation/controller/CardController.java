package com.jazztech.cardholder.presentation.controller;

import com.jazztech.cardholder.presentation.dto.CardResponseDto;
import com.jazztech.cardholder.presentation.dto.CardUpdateResponseDto;
import com.jazztech.cardholder.service.CardSearch;
import com.jazztech.cardholder.service.CardService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    private static final Integer ROUND = 2;

    private final CardService cardService;
    private final CardSearch cardSearch;

    @PostMapping("/{cardHolderId}/cards")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CardResponseDto createCard(
            @PathVariable(value = "cardHolderId") UUID cardHolderId,
            @RequestBody Map<String, Object> requestPayload
    ) {
        final BigDecimal limit = new BigDecimal(requestPayload.get("limit").toString()).setScale(ROUND, RoundingMode.HALF_UP);

        LOGGER.info("Card creation requested");
        return cardService.createCard(cardHolderId, limit);
    }

    @GetMapping("/{cardHolderId}/cards")
    @ResponseStatus(value = HttpStatus.OK)
    public List<CardResponseDto> searchAllCardsByCardHolder(
            @PathVariable(value = "cardHolderId") UUID cardHolderId
    ) {
        LOGGER.info("Card search by Card Holder Id " + cardHolderId + " requested");
        return cardSearch.getAllByCardHolder(cardHolderId);
    }

    @GetMapping("/{cardHolderId}/cards/{cardId}")
    @ResponseStatus(value = HttpStatus.OK)
    public CardResponseDto searchCardByIdAndHolderId(
            @PathVariable(value = "cardHolderId") UUID cardHolderId,
            @PathVariable(value = "cardId") UUID cardId
    ) {
        LOGGER.info("Card search by Card Holder Id " + cardHolderId + " and Card Id " + cardId + " requested");
        return cardSearch.getCardByIdAndHolderId(cardHolderId, cardId);
    }

    @PatchMapping("/{cardHolderId}/cards/{cardId}")
    @ResponseStatus(value = HttpStatus.OK)
    public CardUpdateResponseDto updateCardLimit(
            @PathVariable(value = "cardHolderId") UUID cardHolderId,
            @PathVariable(value = "cardId") UUID cardId,
            @RequestBody Map<String, Object> limit
    ) {
        final BigDecimal newLimit = new BigDecimal(limit.get("limit").toString()).setScale(ROUND, RoundingMode.HALF_UP);

        LOGGER.info("Card update requested");
        return cardService.updateCardLimit(cardHolderId, cardId, newLimit);
    }
}
