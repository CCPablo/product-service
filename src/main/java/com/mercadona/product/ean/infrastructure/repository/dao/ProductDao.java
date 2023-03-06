package com.mercadona.product.ean.infrastructure.repository.dao;

import com.mercadona.product.ean.infrastructure.repository.entity.ProductEntity;

import java.util.List;
import java.util.Optional;

public interface ProductDao {

    Optional<ProductEntity> findById(Long id);

    List<ProductEntity> findAllByProductCode(String productCode);

    Optional<ProductEntity> findByCodes(String productCode, String providerCode, String destinationCode);

    ProductEntity save(ProductEntity productEntity);

    ProductEntity update(ProductEntity productEntity);

}
