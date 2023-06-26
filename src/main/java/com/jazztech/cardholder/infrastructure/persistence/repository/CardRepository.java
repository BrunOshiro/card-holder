package com.jazztech.cardholder.infrastructure.persistence.repository;

import com.jazztech.cardholder.infrastructure.persistence.entity.CardEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CardRepository extends JpaRepository<CardEntity, UUID> {
    @Query("SELECT c FROM CardEntity c WHERE c.cardHolder.id = :cardHolderId")
    List<CardEntity> findByCardHolderId(@Param("cardHolderId") UUID cardHolderId);
}
