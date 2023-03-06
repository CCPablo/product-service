package com.mercadona.product.ean.infrastructure.repository.dao.impl;

import com.mercadona.product.ean.infrastructure.repository.dao.DestinationDao;
import com.mercadona.product.ean.infrastructure.repository.dao.ProductDao;
import com.mercadona.product.ean.infrastructure.repository.dao.ProviderDao;
import com.mercadona.product.ean.infrastructure.repository.entity.DestinationEntity;
import com.mercadona.product.ean.infrastructure.repository.entity.ProductEntity;
import com.mercadona.product.ean.infrastructure.repository.entity.ProviderEntity;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductDaoImpl implements ProductDao {

    private final EntityManager entityManager;

    private final DestinationDao destinationDao;

    private final ProviderDao providerDao;

    public ProductDaoImpl(EntityManager entityManager, DestinationDao destinationDao, ProviderDao providerDao) {
        this.entityManager = entityManager;
        this.destinationDao = destinationDao;
        this.providerDao = providerDao;
    }

    @Override
    public Optional<ProductEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(ProductEntity.class, id));
    }

    @Override
    public List<ProductEntity> findAllByProductCode(String productCode) {
        return entityManager.createNamedQuery("findProductWithProductCode", ProductEntity.class)
                .setParameter("productCode", productCode)
                .getResultList();
    }

    @Override
    public Optional<ProductEntity> findByCodes(String productCode, String providerCode, String destinationCode) {
        return entityManager.createNamedQuery("findProductWithCodes", ProductEntity.class)
                .setParameter("productCode", productCode)
                .setParameter("providerCode", providerCode)
                .setParameter("destinationCode", destinationCode)
                .getResultList()
                .stream().findFirst();
    }

    @Override
    @Transactional
    public ProductEntity save(ProductEntity productEntity) {
        DestinationEntity destinationEntity = destinationDao.findByCodeOrCreateByCode(productEntity.getDestination().getCode());
        ProviderEntity providerEntity = providerDao.findByCodeOrCreateByCode(productEntity.getProvider().getCode());
        productEntity.setDestination(destinationEntity);
        productEntity.setProvider(providerEntity);
        entityManager.persist(productEntity);
        entityManager.flush();
        return productEntity;
    }

    @Override
    @Transactional
    public ProductEntity update(ProductEntity productEntity) {
        DestinationEntity destinationEntity = destinationDao.findByIdOrCreateByCode(
                productEntity.getDestination().getId(),productEntity.getDestination().getCode());
        ProviderEntity providerEntity = providerDao.findByIdOrCreateByCode(
                productEntity.getProvider().getId(), productEntity.getProvider().getCode());
        productEntity.setDestination(destinationEntity);
        productEntity.setProvider(providerEntity);
        entityManager.merge(productEntity);
        entityManager.flush();
        return productEntity;
    }
}
