package com.jazztech.cardholder.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v.1.0.0/credit/card/holder")
@RequiredArgsConstructor
@Validated
public class CardHolderController {
}
