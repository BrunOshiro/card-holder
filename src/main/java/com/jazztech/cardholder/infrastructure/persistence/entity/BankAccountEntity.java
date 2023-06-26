package com.jazztech.cardholder.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Getter
@Table(name = "bank_account")
public class BankAccountEntity {
    @Id
    @GeneratedValue(generator = "uuid2")
    UUID id;

    String account;

    String agency;

    @Column(name = "bank_code")
    String bankCode;

    @Column(name = "created_at")
    @CreationTimestamp
    LocalDateTime createdAt;

    public BankAccountEntity() {
    }

    @Builder(toBuilder = true)
    public BankAccountEntity(
            String account,
            String agency,
            String bankCode
    ) {
        this.account = account;
        this.agency = agency;
        this.bankCode = bankCode;
        this.createdAt = LocalDateTime.now();
    }
}
