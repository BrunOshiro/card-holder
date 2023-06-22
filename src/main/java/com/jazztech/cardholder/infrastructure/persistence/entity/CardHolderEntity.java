package com.jazztech.cardholder.infrastructure.persistence.entity;

import com.jazztech.cardholder.infrastructure.persistence.enums.CardHolderStatusEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Setter
@Table(name = "card_holder")
public class CardHolderEntity {
    private static final Integer ROUND = 2;

    @Id
    @GeneratedValue(generator = "uuid2")
    UUID id;

    @Column(name = "client_id")
    UUID clientId;

    @Column(name = "credit_analysis_id")
    UUID creditAnalysisId;

    @Enumerated(EnumType.STRING)
    CardHolderStatusEnum status;

    @Column(name = "credit_limit")
    BigDecimal creditLimit;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bank_account_id", referencedColumnName = "id")
    BankAccountEntity bankAccount;

    @Column(name = "created_at")
    @CreationTimestamp
    LocalDateTime createdAt;

    public CardHolderEntity() {
    }

    @Builder(toBuilder = true)
    public CardHolderEntity(
            UUID clientId,
            UUID creditAnalysisId,
            CardHolderStatusEnum status,
            BigDecimal creditLimit,
            BankAccountEntity bankAccount
    ) {
        this.clientId = clientId;
        this.creditAnalysisId = creditAnalysisId;
        this.status = status;
        this.creditLimit = creditLimit.setScale(ROUND, RoundingMode.HALF_UP);
        this.createdAt = LocalDateTime.now();
        this.bankAccount = bankAccount;
    }
}
