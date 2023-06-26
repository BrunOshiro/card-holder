package com.jazztech.cardholder.domain;

import java.math.BigDecimal;
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
    public Boolean isLimitAvailable(BigDecimal creditLimitRequested, BigDecimal creditLimitApproved) {
        return creditLimitApproved.compareTo(creditLimitRequested) >= 0;
    }

    public String generateCreditCardDueDate() {
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();

        LocalDate saveDate = LocalDate.of(currentYear, currentMonth, 30);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");

        return saveDate.format(formatter);
    }

    public String generateCreditCardCvv() {
        Random random = new Random();
        StringBuilder cvv = new StringBuilder();

        while (cvv.length() < 3) {
            cvv.append(random.nextInt(10));
        }

        return cvv.toString();
    }

    public String generateCreditCardNumber() {
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
