package com.jazztech.cardholder.infrastructure.persistence.repository;

import com.jazztech.cardholder.infrastructure.persistence.entity.CardHolderEntity;
import com.jazztech.cardholder.infrastructure.persistence.enums.CardHolderStatusEnum;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardHolderRepository extends JpaRepository<CardHolderEntity, UUID> {
    CardHolderEntity findByClientId(UUID clientId);

    List<CardHolderEntity> findByStatusEquals(CardHolderStatusEnum status);
}
