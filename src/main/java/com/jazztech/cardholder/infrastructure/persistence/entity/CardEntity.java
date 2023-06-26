package com.jazztech.cardholder.infrastructure.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Setter
@Table(name = "card")
public class CardEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "card_holder_id", referencedColumnName = "id")
    CardHolderEntity cardHolder;

    @Column(name = "credit_limit")
    BigDecimal creditLimit;

    @Column(name = "card_number")
    String cardNumber;

    String cvv;

    @Column(name = "due_date")
    String dueDate;

    @CreationTimestamp
    @Column(name = "created_at")
    LocalDateTime createdAt;

    public CardEntity() {
    }

    public CardEntity(
            CardHolderEntity cardHolder,
            BigDecimal creditLimit,
            String cardNumber,
            String cvv,
            String dueDate
    ) {
        this.cardHolder = cardHolder;
        this.creditLimit = creditLimit;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.dueDate = dueDate;
    }
}
