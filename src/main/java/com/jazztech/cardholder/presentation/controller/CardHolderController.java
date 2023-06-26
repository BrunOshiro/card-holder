package com.jazztech.cardholder.presentation.controller;

import com.jazztech.cardholder.presentation.dto.CardHolderRequestDto;
import com.jazztech.cardholder.presentation.dto.CardHolderResponseDto;
import com.jazztech.cardholder.service.CardHolderSearch;
import com.jazztech.cardholder.service.CardHolderService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1.0/card-holders")
@RequiredArgsConstructor
@Validated
public class CardHolderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CardHolderController.class);

    private final CardHolderService cardHolderService;
    private final CardHolderSearch cardHolderSearch;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CardHolderResponseDto createCardHolder(@RequestBody @Valid CardHolderRequestDto cardHolderRequestDto) {
        LOGGER.info("Card Holder creation requested: " + cardHolderRequestDto.toString());
        return cardHolderService.createCardHolder(cardHolderRequestDto);
    }

    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<CardHolderResponseDto> searchAllCardHolders(
            @RequestParam(value = "status", required = false) String status
    ) {
        if (status != null) {
            LOGGER.info("Search Card Holders by status " + status + " requested");
            return cardHolderSearch.getAllByStatus(status);
        } else {
            LOGGER.info("Search All Card Holders requested");
            return cardHolderSearch.getAll();
        }
    }
}
