package com.jazztech.infrasctructure;

import com.jazztech.cardholder.infrastructure.creditanalysisapi.dto.CreditAnalysisDto;
import com.jazztech.cardholder.infrastructure.persistence.entity.CardHolderEntity;
import com.jazztech.cardholder.infrastructure.persistence.enums.CardHolderStatusEnum;
import com.jazztech.cardholder.presentation.dto.CardHolderRequestDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Factory {
    public static CreditAnalysisDto creditAnalysisDtoFactory() {
        return CreditAnalysisDto.builder()
                .id(UUID.fromString("5619f798-d929-47ab-9b9d-4126905c2e11"))
                .approved(true)
                .approvedLimit(BigDecimal.valueOf(3000.00))
                .withdraw(BigDecimal.valueOf(300.00))
                .annualInterest(BigDecimal.valueOf(0.15))
                .clientId(UUID.fromString("abf5d31a-ebb3-47e0-a441-f0487dd804e9"))
                .date(LocalDateTime.parse("2023-05-30T10:16:18.34429"))
                .build();
    }

    public static CardHolderEntity cardHolderEntityFactory() {
        return CardHolderEntity.builder()
                .clientId(UUID.fromString("abf5d31a-ebb3-47e0-a441-f0487dd804e9"))
                .creditAnalysisId(UUID.fromString("5619f798-d929-47ab-9b9d-4126905c2e11"))
                .status(CardHolderStatusEnum.valueOf("ACTIVE"))
                .creditLimit(BigDecimal.valueOf(3000.00))
                .bankAccount("12345-6")
                .bankAgency("0001")
                .bankCode("242")
                .build();
    }

    public static CardHolderRequestDto cardHolderRequestDtoFactory() {
        return CardHolderRequestDto.builder()
                .clientId(UUID.fromString("abf5d31a-ebb3-47e0-a441-f0487dd804e9"))
                .creditAnalysisId(UUID.fromString("5619f798-d929-47ab-9b9d-4126905c2e11"))
                .bankAccount(CardHolderRequestDto.BankAccount.builder()
                        .account("12345-6")
                        .agency("0001")
                        .bankCode("242")
                        .build())
                .build();
    }
}
