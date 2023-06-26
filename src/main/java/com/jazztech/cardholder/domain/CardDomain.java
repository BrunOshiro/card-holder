package com.jazztech.cardholder.domain;

import com.jazztech.cardholder.infrastructure.handler.exception.CreditLimitNotAvailable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;
import lombok.Builder;

@Builder(toBuilder = true)
public record CardDomain(
        UUID cardHolderId,
        BigDecimal creditLimit,
        String cardNumber,
        String cvv,
        String dueDate
) {
    private static final Integer ROUND = 2;

    public BigDecimal limitAvailable(BigDecimal creditLimitApproved, BigDecimal creditLimitRequested, BigDecimal creditLimitUsed) {
        final BigDecimal creditLimitUsedWithRequest = creditLimitUsed.add(creditLimitRequested);
        return creditLimitApproved.subtract(creditLimitUsedWithRequest);
    }

    public CardDomain createCardDomain(UUID cardHolderId, BigDecimal limitRequested, BigDecimal limitAvailable) {
        if (!isCreditLimitRequestedValid(limitAvailable, limitRequested)) {
            throw new CreditLimitNotAvailable("Credit limit requested is less than the limit available to the card holder.");
        }

        return CardDomain.builder()
                .creditLimit(limitRequested.setScale(ROUND, RoundingMode.HALF_UP))
                .cardHolderId(cardHolderId)
                .cardNumber(generateCreditCardNumber())
                .cvv(generateCreditCardCvv())
                .dueDate(generateCreditCardDueDate())
                .build();
    }

    private Boolean isCreditLimitRequestedValid(BigDecimal creditLimitAvailable, BigDecimal creditLimitRequested) {
        return creditLimitAvailable.compareTo(creditLimitRequested) >= 0;
    }

    private String generateCreditCardDueDate() {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();
        int dueYear = currentYear + 5;

        LocalDate dateToUse = LocalDate.of(dueYear, currentMonth, 30);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");

        return dateToUse.format(formatter);
    }

    private String generateCreditCardCvv() {
        Random random = new Random();
        StringBuilder cvv = new StringBuilder();

        while (cvv.length() < 3) {
            cvv.append(random.nextInt(10));
        }

        return cvv.toString();
    }

    private String generateCreditCardNumber() {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder();
        String issuer = "American Express";

        cardNumber.append(getIssuerDigits(issuer));

        while (cardNumber.length() < 15) {
            cardNumber.append(random.nextInt(10));
        }

        int checkDigit = calculateLuhnCheckDigit(cardNumber.toString());

        cardNumber.append(checkDigit);

        return cardNumber.toString();
    }

    private static String getIssuerDigits(String issuer) {
        if (issuer.equalsIgnoreCase("Visa")) {
            return "4";
        } else if (issuer.equalsIgnoreCase("Mastercard")) {
            int prefix = getRandomNumberInRange(51, 55);
            return String.valueOf(prefix);
        } else if (issuer.equalsIgnoreCase("American Express")) {
            int prefix = getRandomNumberInRange(34, 37);
            return String.valueOf(prefix);
        }

        return "";
    }

    private static int getRandomNumberInRange(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

    private static int calculateLuhnCheckDigit(String cardNumber) {
        int sum = 0;
        boolean alternate = false;

        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Integer.parseInt(cardNumber.substring(i, i + 1));

            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = digit % 10 + 1;
                }
            }

            sum += digit;
            alternate = !alternate;
        }

        int checkDigit = 10 - (sum % 10);
        return (checkDigit == 10) ? 0 : checkDigit;
    }
}
