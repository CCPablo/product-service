package com.mercadona.product.ean.infrastructure.repository.dao.impl;

import com.mercadona.product.ean.infrastructure.repository.dao.DestinationDao;
import com.mercadona.product.ean.infrastructure.repository.dao.ProviderDao;
import com.mercadona.product.ean.infrastructure.repository.entity.DestinationEntity;
import com.mercadona.product.ean.infrastructure.repository.entity.ProductEntity;
import com.mercadona.product.ean.infrastructure.repository.entity.ProviderEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ProductDaoImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private DestinationDao destinationDao;

    @Mock
    private ProviderDao providerDao;

    @InjectMocks
    private ProductDaoImpl productDao;

    @Test
    public void givenExistingProductEntity_whenFindById_thenReturnsProductEntity() {
        ProductEntity productEntity = TestData.getProductEntityA();
        Mockito.when(entityManager.find(ProductEntity.class, productEntity.getId())).thenReturn(productEntity);

        Optional<ProductEntity> result = productDao.findById(productEntity.getId());

        assertProductEntityEquals(productEntity, result.get());
    }

    @Test
    public void givenNonExistingProductEntity_whenFindById_thenReturnsEmptyOptional() {
        ProductEntity productEntity = TestData.getProductEntityA();
        Mockito.when(entityManager.find(ProductEntity.class, productEntity.getId())).thenReturn(null);

        Optional<ProductEntity> result = productDao.findById(productEntity.getId());

        assertTrue(result.isEmpty());
    }

    private void assertProductEntityEquals(ProductEntity productEntityA, ProductEntity productEntityB) {
        assertEquals(productEntityA.getId(), productEntityB.getId());
        assertEquals(productEntityA.getEanCode(), productEntityB.getEanCode());
        assertEquals(productEntityA.getProductCode(), productEntityB.getProductCode());
        assertDestinationEntityEquals(productEntityA.getDestination(), productEntityB.getDestination());
        assertProviderEntityEquals(productEntityA.getProvider(), productEntityB.getProvider());
    }

    private void assertDestinationEntityEquals(DestinationEntity destinationEntityA, DestinationEntity destinationEntityB) {
        assertEquals(destinationEntityA.getId(), destinationEntityB.getId());
        assertEquals(destinationEntityA.getCode(), destinationEntityB.getCode());
        assertEquals(destinationEntityA.getName(), destinationEntityB.getName());
    }

    private void assertProviderEntityEquals(ProviderEntity providerEntityA, ProviderEntity providerEntityB) {
        assertEquals(providerEntityA.getId(), providerEntityB.getId());
        assertEquals(providerEntityA.getCode(), providerEntityB.getCode());
        assertEquals(providerEntityA.getName(), providerEntityB.getName());
    }

    interface TestData {

        Long PRODUCT_ID_A = 1L;
        String EAN_CODE_A = "12345678901234";
        Integer PROVIDER_ID_A = 1;
        String PROVIDER_CODE_A = "1234567";
        String PROVIDER_NAME_A = "Provider";
        Integer DESTINATION_ID_A = 1;
        String DESTINATION_CODE_A = "1";
        String DESTINATION_NAME_A = "Destination";
        String PRODUCT_CODE_A = "12345";
        String UPDATED_PRODUCT_CODE_A = "12346";

        static ProductEntity getProductEntityA() {
            ProviderEntity providerEntity = new ProviderEntity(PROVIDER_ID_A, PROVIDER_NAME_A, PROVIDER_CODE_A);
            DestinationEntity destinationEntity = new DestinationEntity(DESTINATION_ID_A, DESTINATION_NAME_A, DESTINATION_CODE_A);
            return new ProductEntity(PRODUCT_ID_A, EAN_CODE_A, PRODUCT_CODE_A, destinationEntity, providerEntity);
        }
    }
}