package com.jazztech.cardholder.service;

import static com.jazztech.cardholder.service.constants.CardConstants.AMERICAN_EXPRESS_MAX_RANGE;
import static com.jazztech.cardholder.service.constants.CardConstants.AMERICAN_EXPRESS_MIN_RANGE;
import static com.jazztech.cardholder.service.constants.CardConstants.CARD_NUMBER_GROUP_SIZE;
import static com.jazztech.cardholder.service.constants.CardConstants.CARD_NUMBER_LENGTH;
import static com.jazztech.cardholder.service.constants.CardConstants.CVV_MAX_LENGTH;
import static com.jazztech.cardholder.service.constants.CardConstants.DIGIT_NINE;
import static com.jazztech.cardholder.service.constants.CardConstants.ISSUER;
import static com.jazztech.cardholder.service.constants.CardConstants.LUHN_DOUBLE_FACTOR;
import static com.jazztech.cardholder.service.constants.CardConstants.MASTERCARD_MAX_RANGE;
import static com.jazztech.cardholder.service.constants.CardConstants.MASTERCARD_MIN_RANGE;
import static com.jazztech.cardholder.service.constants.CardConstants.MODULUS;
import static com.jazztech.cardholder.service.constants.CardConstants.RANDOM_NUM_RANGE;
import static com.jazztech.cardholder.service.constants.CardConstants.ROUND;

import com.jazztech.cardholder.domain.entity.Card;
import com.jazztech.cardholder.infrastructure.handler.exception.CardHolderInactive;
import com.jazztech.cardholder.infrastructure.handler.exception.CardHolderNotFound;
import com.jazztech.cardholder.infrastructure.handler.exception.CreditLimitNotAvailable;
import com.jazztech.cardholder.infrastructure.persistence.entity.CardEntity;
import com.jazztech.cardholder.infrastructure.persistence.entity.CardHolderEntity;
import com.jazztech.cardholder.infrastructure.persistence.enums.CardHolderStatusEnum;
import com.jazztech.cardholder.infrastructure.persistence.mapper.CardMapper;
import com.jazztech.cardholder.infrastructure.persistence.repository.CardHolderRepository;
import com.jazztech.cardholder.infrastructure.persistence.repository.CardRepository;
import com.jazztech.cardholder.presentation.dto.CardResponseDto;
import com.jazztech.cardholder.service.constants.CardConstants;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        final CardHolderEntity cardHolder = getCardHolder(cardHolderId);
        final BigDecimal limitAvailable = getCreditAvailableToTheCardHolder(cardHolder);
        final Card createdCard = createCard(cardHolder, limitRequested, limitAvailable);
        final CardEntity savedCardEntity = cardRepository.save(cardMapper.domainToEntity(createdCard));
        LOGGER.info("Card created: {}", savedCardEntity);
        return cardMapper.entityToDto(savedCardEntity);
    }

    private BigDecimal getCreditAvailableToTheCardHolder(CardHolderEntity cardHolder) {
        final BigDecimal creditLimitAvailable = cardHolder.getCreditLimitAvailable();

        LOGGER.info("Limit available to the Card Holder " + cardHolder.getId() + " is: " + creditLimitAvailable);
        return creditLimitAvailable;
    }

    private Card createCard(CardHolderEntity cardHolder, BigDecimal limitRequested, BigDecimal limitAvailable) {
        if (!(limitAvailable.compareTo(limitRequested) >= 0)) {
            throw new CreditLimitNotAvailable("Credit limit requested is less than the limit available to the card holder.");
        }

        final BigDecimal newCreditLimitAvailable = limitAvailable.subtract(limitRequested);
        cardHolder.setCreditLimitAvailable(newCreditLimitAvailable);
        cardHolderRepository.save(cardHolder);
        LOGGER.info("Credit limit available updated to the Card Holder " + cardHolder.getId() + " is: " + newCreditLimitAvailable);

        return Card.builder()
                .creditLimit(limitRequested.setScale(ROUND, RoundingMode.HALF_UP))
                .cardHolderId(cardHolder.getId())
                .cardNumber(generateCreditCardNumber())
                .cvv(generateCreditCardCvv())
                .dueDate(generateCreditCardDueDate())
                .build();
    }

    private CardHolderEntity getCardHolder(UUID cardHolderId) {
        final CardHolderEntity cardHolder = cardHolderRepository.findById(cardHolderId)
                .orElseThrow(() -> new CardHolderNotFound("Card Holder not found with id: " + cardHolderId));
        if (cardHolder.getStatus() == CardHolderStatusEnum.INACTIVE) {
            throw new CardHolderInactive("Card Holder Id " + cardHolderId + " is inactive.");
        }
        return cardHolder;
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

        final int checkDigit = CardConstants.DIGIT_TEN - (sum % MODULUS);
        return (checkDigit == CardConstants.DIGIT_TEN) ? 0 : checkDigit;
    }

    //TODO: subtract the used limit from the CardHolderEntity.availableLimit

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

        cardNumber.append(getIssuerDigits(ISSUER));

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
