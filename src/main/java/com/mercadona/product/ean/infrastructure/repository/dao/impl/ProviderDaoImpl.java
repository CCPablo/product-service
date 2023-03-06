package com.mercadona.product.ean.infrastructure.repository.dao.impl;

import com.mercadona.product.ean.infrastructure.repository.dao.ProviderDao;
import com.mercadona.product.ean.infrastructure.repository.entity.ProviderEntity;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class ProviderDaoImpl implements ProviderDao {

    private final EntityManager entityManager;

    public ProviderDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<ProviderEntity> findById(Integer id) {
        return Optional.ofNullable(entityManager.find(ProviderEntity.class, id));
    }

    @Override
    @Transactional
    public ProviderEntity findByCodeOrCreateByCode(String code) {
        Optional<ProviderEntity> providerEntityOptional = entityManager.createNamedQuery("findProviderWithCode", ProviderEntity.class)
                .setParameter("code", code)
                .getResultList()
                .stream().findFirst();
        if (providerEntityOptional.isPresent()) {
            return providerEntityOptional.get();
        } else {
            ProviderEntity providerEntity = new ProviderEntity(code);
            try {
                entityManager.persist(providerEntity);
            } catch (Exception e) {
                return entityManager.find(ProviderEntity.class, providerEntity.getId());
            }
            entityManager.flush();
            return providerEntity;
        }
    }

    @Override
    public ProviderEntity findByIdOrCreateByCode(Integer id, String code) {
        if(id == null) {
            return findByCodeOrCreateByCode(code);
        }
        return findById(id)
                .orElse(findByCodeOrCreateByCode(code));
    }
}
