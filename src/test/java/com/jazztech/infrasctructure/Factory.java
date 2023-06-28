package com.jazztech.infrasctructure;

import com.jazztech.cardholder.infrastructure.creditanalysisapi.dto.CreditAnalysisDto;
import com.jazztech.cardholder.infrastructure.persistence.entity.BankAccountEntity;
import com.jazztech.cardholder.infrastructure.persistence.entity.CardEntity;
import com.jazztech.cardholder.infrastructure.persistence.entity.CardHolderEntity;
import com.jazztech.cardholder.infrastructure.persistence.enums.CardHolderStatusEnum;
import com.jazztech.cardholder.presentation.dto.CardHolderRequestDto;
import com.jazztech.cardholder.presentation.dto.CardResponseDto;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

public class Factory {
    private static final Random random = new Random();

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
                .creditLimitAvailable(BigDecimal.valueOf(3000.00))
                .bankAccount(BankAccountEntity.builder()
                        .account("12345-6")
                        .agency("0001")
                        .bankCode("242")
                        .build())
                .build();
    }

    public static CardEntity cardEntityFactory() {
        return CardEntity.builder()
                .cardHolder(cardHolderEntityFactory())
                .cardNumber("1234-5678-9012-3456")
                .dueDate("06/2028")
                .cvv("123")
                .build();
    }

    public static CardResponseDto cardResponseDtoFactory() {
        return CardResponseDto.builder()
                .cardNumber("1234-5678-9012-3456")
                .dueDate("06/2028")
                .cvv("123")
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

    public static List<CardHolderEntity> cardHolderEntityListFactory(Integer listSize) {
        List<CardHolderEntity> cardHolderEntityList = new ArrayList<>();
        CardHolderStatusEnum[] statusValues = CardHolderStatusEnum.values();

        IntStream.range(0, listSize)
                .forEach(i -> {
                    int randomIndex = random.nextInt(statusValues.length);
                    BigDecimal randomAmount = generatedRandomAmount();

                    cardHolderEntityList.add(CardHolderEntity.builder()
                            .clientId(UUID.randomUUID())
                            .creditAnalysisId(UUID.randomUUID())
                            .status(statusValues[randomIndex])
                            .creditLimit(randomAmount)
                            .creditLimitAvailable(randomAmount)
                            .bankAccount(BankAccountEntity.builder()
                                    .account(generateRandomNumber(5) + "-" + generateRandomNumber(1))
                                    .agency(generateRandomNumber(4))
                                    .bankCode(generateRandomNumber(3))
                                    .build())
                            .build());
                });
        return cardHolderEntityList;
    }

    private static BigDecimal generatedRandomAmount() {
        double min = 0.00;
        double max = 100000.00;
        double randomValue = min + (max - min) * random.nextDouble();
        return BigDecimal.valueOf(randomValue).setScale(2, RoundingMode.HALF_UP);
    }

    public static String generateRandomNumber(int maxDigits) {
        if (maxDigits <= 0) {
            throw new IllegalArgumentException("The number of digits must be greater than zero.");
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < maxDigits; i++) {
            int digit = random.nextInt(10);
            sb.append(digit);
        }

        return sb.toString();
    }
}
