package com.mercadona.product.ean.infrastructure.repository.dao.impl;

import com.mercadona.product.ean.infrastructure.repository.dao.DestinationDao;
import com.mercadona.product.ean.infrastructure.repository.entity.DestinationEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class DestinationDaoImpl implements DestinationDao {

    private final EntityManager entityManager;

    public DestinationDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Optional<DestinationEntity> findById(Integer id) {
        return Optional.ofNullable(entityManager.find(DestinationEntity.class, id));
    }

    @Override
    @Transactional
    public DestinationEntity findByCodeOrCreateByCode(String code) {
        Optional<DestinationEntity> destinationEntityOptional = entityManager.createNamedQuery("findDestinationWithCode", DestinationEntity.class)
                .setParameter("code", code)
                .getResultList()
                .stream().findFirst();
        if (destinationEntityOptional.isPresent()) {
            return destinationEntityOptional.get();
        } else {
            DestinationEntity destinationEntity = new DestinationEntity(code);
            try {
                entityManager.persist(destinationEntity);
            } catch (PersistenceException e) {
                return entityManager.find(DestinationEntity.class, destinationEntity.getId());
            }
            entityManager.flush();
            return destinationEntity;
        }
    }

    @Override
    public DestinationEntity findByIdOrCreateByCode(Integer id, String code) {
        if(id == null) {
            return findByCodeOrCreateByCode(code);
        }
        return findById(id)
                .orElse(findByCodeOrCreateByCode(code));
    }
}
