package com.jazztech.cardholder.infrastructure.persistence.repository;

import com.jazztech.cardholder.infrastructure.persistence.entity.CardEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<CardEntity, UUID> {
    List<CardEntity> findByCardHolderId(UUID cardHolderId);
}
