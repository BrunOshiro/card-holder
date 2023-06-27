package com.jazztech.cardholder.domain;

import com.jazztech.cardholder.infrastructure.handler.exception.CreditLimitNotAvailable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@Builder(toBuilder = true)
public class CardDomain {

    private static final Integer ROUND = 2;
    private static final int AMERICAN_EXPRESS_MIN_RANGE = 34;
    private static final int AMERICAN_EXPRESS_MAX_RANGE = 37;
    private static final int MASTERCARD_MIN_RANGE = 51;
    private static final int MASTERCARD_MAX_RANGE = 55;
    private static final int RANDOM_NUM_RANGE = 10;
    private static final int MODULUS = 10;
    private static final int DIGIT_TEN = 10;
    private static final int DIGIT_NINE = 9;
    private static final int CVV_MAX_LENGTH = 3;
    private static final int LUHN_DOUBLE_FACTOR = 2;
    private static final int CARD_NUMBER_LENGTH = 15;
    private static final int CARD_NUMBER_GROUP_SIZE = 4;

    UUID cardHolderId;
    BigDecimal creditLimit;
    String cardNumber;
    String cvv;
    String dueDate;

    public CardDomain() {
    }

    public CardDomain(
            UUID cardHolderId,
            BigDecimal creditLimit,
            String cardNumber,
            String cvv,
            String dueDate
    ) {
        this.cardHolderId = cardHolderId;
        this.creditLimit = creditLimit;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.dueDate = dueDate;
    }

    private static String getIssuerDigits(String issuer) {
        if ("Visa".equalsIgnoreCase(issuer)) {
            return "4";
        } else if ("Mastercard".equalsIgnoreCase(issuer)) {
            final int prefix = getRandomNumberInRange(MASTERCARD_MIN_RANGE, MASTERCARD_MAX_RANGE);
            return String.valueOf(prefix);
        } else if ("American Express".equalsIgnoreCase(issuer)) {
            final int prefix = getRandomNumberInRange(AMERICAN_EXPRESS_MIN_RANGE, AMERICAN_EXPRESS_MAX_RANGE);
            return String.valueOf(prefix);
        }

        return "";
    }

    private static int getRandomNumberInRange(int min, int max) {
        final Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

    private static int calculateLuhnCheckDigit(String cardNumber) {
        int sum = 0;
        boolean alternate = false;

        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Integer.parseInt(cardNumber.substring(i, i + 1));

            if (alternate) {
                digit *= LUHN_DOUBLE_FACTOR;
                if (digit > DIGIT_NINE) {
                    digit = digit % MODULUS + 1;
                }
            }

            sum += digit;
            alternate = !alternate;
        }

        final int checkDigit = DIGIT_TEN - (sum % MODULUS);
        return (checkDigit == DIGIT_TEN) ? 0 : checkDigit;
    }

    public BigDecimal limitAvailable(BigDecimal creditLimitApproved, BigDecimal creditLimitRequested, BigDecimal creditLimitUsed) {
        final BigDecimal creditLimitUsedWithRequest = creditLimitUsed.add(creditLimitRequested).setScale(ROUND, RoundingMode.HALF_UP);
        return creditLimitApproved.subtract(creditLimitUsedWithRequest).setScale(ROUND, RoundingMode.HALF_UP);
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
        final LocalDate currentDate = LocalDate.now();
        final int currentMonth = currentDate.getMonthValue();
        final int currentYear = currentDate.getYear();
        final int dueYear = currentYear + 5;

        final LocalDate dateToUse = LocalDate.of(dueYear, currentMonth, currentDate.lengthOfMonth());

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");

        return dateToUse.format(formatter);
    }

    private String generateCreditCardCvv() {
        final Random random = new Random();
        final StringBuilder cvv = new StringBuilder();

        while (cvv.length() < CVV_MAX_LENGTH) {
            cvv.append(random.nextInt(RANDOM_NUM_RANGE));
        }

        return cvv.toString();
    }

    private String generateCreditCardNumber() {
        final Random random = new Random();
        final StringBuilder cardNumber = new StringBuilder();
        final String issuer = "American Express";

        cardNumber.append(getIssuerDigits(issuer));

        while (cardNumber.length() < CARD_NUMBER_LENGTH) {
            cardNumber.append(random.nextInt(RANDOM_NUM_RANGE));
        }

        final int checkDigit = calculateLuhnCheckDigit(cardNumber.toString());
        cardNumber.append(checkDigit);

        for (int i = CARD_NUMBER_GROUP_SIZE; i < cardNumber.length(); i += CARD_NUMBER_GROUP_SIZE + 1) {
            cardNumber.insert(i, " ");
        }

        return cardNumber.toString();
    }
}
