package com.mercadona.product.ean.infrastructure.repository.dao;

import com.mercadona.product.ean.infrastructure.repository.entity.DestinationEntity;

import java.util.Optional;

public interface DestinationDao {

    Optional<DestinationEntity> findById(Integer id);

    DestinationEntity findByCodeOrCreateByCode(String code);

    DestinationEntity findByIdOrCreateByCode(Integer id, String code);
}
