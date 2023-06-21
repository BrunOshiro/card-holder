package com.jazztech.cardholder.infrastructure.persistence.entity;

import com.jazztech.cardholder.infrastructure.persistence.enums.CardHolderStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "card_holder")
public class CardHolderEntity {
    private static final Integer ROUND = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(name = "client_id")
    UUID clientId;

    @Column(name = "credit_analysis_id")
    UUID creditAnalysisId;

    @Enumerated(EnumType.STRING)
    CardHolderStatusEnum status;

    @Column(name = "credit_limit")
    BigDecimal creditLimit;

    @Column(name = "bank_account")
    String bankAccount;

    @Column(name = "bank_agency")
    String bankAgency;

    @Column(name = "bank_code")
    String bankCode;

    @Column(name = "created_at")
    LocalDateTime createdAt;

    public CardHolderEntity() {
    }

    public CardHolderEntity(
            UUID clientId,
            UUID creditAnalysisId,
            CardHolderStatusEnum status,
            BigDecimal creditLimit,
            String bankAccount,
            String bankAgency,
            String bankCode
    ) {
        this.clientId = clientId;
        this.creditAnalysisId = creditAnalysisId;
        this.status = status;
        this.creditLimit = creditLimit.setScale(ROUND, RoundingMode.HALF_UP);
        this.bankAccount = bankAccount;
        this.bankAgency = bankAgency;
        this.bankCode = bankCode;
        this.createdAt = LocalDateTime.now();
    }
}
