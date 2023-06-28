package com.jazztech.cardholder.service;

import com.jazztech.cardholder.domain.entity.Card;
import com.jazztech.cardholder.infrastructure.handler.exception.CardHolderNotFound;
import com.jazztech.cardholder.infrastructure.handler.exception.CreditLimitNotAvailable;
import com.jazztech.cardholder.infrastructure.persistence.entity.CardEntity;
import com.jazztech.cardholder.infrastructure.persistence.mapper.CardMapper;
import com.jazztech.cardholder.infrastructure.persistence.repository.CardHolderRepository;
import com.jazztech.cardholder.infrastructure.persistence.repository.CardRepository;
import com.jazztech.cardholder.presentation.dto.CardResponseDto;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class CardService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CardService.class);

    private final CardHolderRepository cardHolderRepository;
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    @Transactional
    public CardResponseDto createCard(UUID cardHolderId, BigDecimal limitRequested) {
        final BigDecimal limitAvailable = getCreditAvailableToTheCardHolder(cardHolderId, limitRequested);
        final Card createdCard = card.createCardDomain(cardHolderId, limitRequested, limitAvailable);
        final CardEntity savedCardEntity = cardRepository.save(cardMapper.domainToEntity(createdCard));
        LOGGER.info("Card created: {}", savedCardEntity);
        return cardMapper.entityToDto(savedCardEntity);
    }

    //TODO: create/change the method to return the available limit to the card holder
    //TODO: substract the used limit from the CardHolderEntity.availableLimit
    private BigDecimal getCreditAvailableToTheCardHolder(UUID cardHolderId, BigDecimal limitRequested) {

        //TODO: user orElseThrow instead of if/else
        if (cardHolderRepository.findById(cardHolderId).isEmpty()) {
            throw new CardHolderNotFound("Card Holder not found with the provided Id: " + cardHolderId);
        }

        final BigDecimal creditLimitApproved = cardHolderRepository.findById(cardHolderId).get().getCreditLimit();
        final BigDecimal creditLimitUsed;

        if (cardRepository.findByCardHolderId(cardHolderId) != null) {
            final List<CardEntity> cardEntityList = cardRepository.findByCardHolderId(cardHolderId);
            creditLimitUsed = cardEntityList.stream().map(CardEntity::getCreditLimit).reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            creditLimitUsed = BigDecimal.ZERO;
        }

        return card.limitAvailable(creditLimitApproved, limitRequested, creditLimitUsed);
    }

    public BigDecimal limitAvailable(BigDecimal creditLimitApproved, BigDecimal creditLimitRequested, BigDecimal creditLimitUsed) {
        final BigDecimal creditLimitUsedWithRequest = creditLimitUsed.add(creditLimitRequested).setScale(ROUND, RoundingMode.HALF_UP);
        return creditLimitApproved.subtract(creditLimitUsedWithRequest);
    }

    public Card createCardDomain(UUID cardHolderId, BigDecimal limitRequested, BigDecimal limitAvailable) {
        if (!isCreditLimitRequestedValid(limitAvailable, limitRequested)) {
            throw new CreditLimitNotAvailable("Credit limit requested is less than the limit available to the card holder.");
        }

        return Card.builder()
                .creditLimit(limitRequested.setScale(ROUND, RoundingMode.HALF_UP))
                .cardHolderId(cardHolderId)
                .cardNumber(generateCreditCardNumber())
                .cvv(generateCreditCardCvv())
                .dueDate(generateCreditCardDueDate())
                .build();
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
