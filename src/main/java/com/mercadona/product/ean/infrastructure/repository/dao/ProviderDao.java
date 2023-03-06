package com.mercadona.product.ean.infrastructure.repository.dao;

import com.mercadona.product.ean.infrastructure.repository.entity.ProviderEntity;

import java.util.Optional;

public interface ProviderDao {

    Optional<ProviderEntity> findById(Integer id);

    ProviderEntity findByCodeOrCreateByCode(String code);

    ProviderEntity findByIdOrCreateByCode(Integer id, String code);
}
