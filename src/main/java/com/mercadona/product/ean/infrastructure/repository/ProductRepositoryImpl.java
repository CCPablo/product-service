package com.mercadona.product.ean.infrastructure.repository;

import com.mercadona.product.ean.domain.aggregate.Product;
import com.mercadona.product.ean.domain.repository.IProductRepository;
import com.mercadona.product.ean.infrastructure.repository.dao.ProductDao;
import com.mercadona.product.ean.infrastructure.repository.entity.ProductEntity;
import com.mercadona.product.ean.infrastructure.repository.exception.ResourceAlreadyExistsException;
import com.mercadona.product.ean.infrastructure.repository.exception.ResourceNotFoundException;
import com.mercadona.product.ean.infrastructure.repository.mapper.AggregateMapper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepositoryImpl implements IProductRepository {

    private final ProductDao productDao;

    public ProductRepositoryImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Cacheable(value = "productsById", key = "#id")
    @Override
    public Product findById(Long id) {
        Optional<ProductEntity> productEntityOptional = productDao.findById(id);
        if (productEntityOptional.isEmpty()) {
            throw new ResourceNotFoundException("Product not found");
        }
        return AggregateMapper.productEntityToProductAggregate(productEntityOptional.get());
    }

    @Override
    public List<Product> findAllByProductCode(String productCode) {
        return productDao.findAllByProductCode(productCode)
                .stream()
                .map(AggregateMapper::productEntityToProductAggregate)
                .toList();
    }

    @Cacheable(value = "productsByCodes", key = "{#productCode, #destinationCode, #providerCode}")
    @Override
    public Product findByDetails(String productCode, String destinationCode, String providerCode) {
        Optional<ProductEntity> productEntityOptional = productDao.findByCodes(productCode, providerCode, destinationCode);
        if (productEntityOptional.isEmpty()) {
            throw new ResourceNotFoundException("Product not found");
        }
        return AggregateMapper.productEntityToProductAggregate(productEntityOptional.get());
    }

    @Override
    public Product save(Product product) {
        ProductEntity productEntity = AggregateMapper.productAggregateToProductEntity(product);
        try {
            productEntity = productDao.save(productEntity);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceAlreadyExistsException("Product EAN already exists. EAN " + product.getEanCode());
        }
        return AggregateMapper.productEntityToProductAggregate(productEntity);
    }

    @Override
    public Product update(Product product) {
        ProductEntity productEntity = AggregateMapper.productAggregateToProductEntity(product);
        try {
            productEntity = productDao.update(productEntity);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceAlreadyExistsException("Product EAN already exists. EAN " + product.getEanCode());
        }
        return AggregateMapper.productEntityToProductAggregate(productEntity);
    }

}
