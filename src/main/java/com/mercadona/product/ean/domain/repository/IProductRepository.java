package com.mercadona.product.ean.domain.repository;

import com.mercadona.product.ean.domain.aggregate.Product;

import java.util.List;

public interface IProductRepository {

    Product findById(Long id);

    List<Product> findAllByProductCode(String productCode);

    Product findByDetails(String productCode, String destinationCode, String providerCode);

    Product save(Product product);

    Product update(Product product);
}
