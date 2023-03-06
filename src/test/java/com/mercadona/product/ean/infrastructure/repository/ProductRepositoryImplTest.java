package com.mercadona.product.ean.infrastructure.repository;

import com.mercadona.product.ean.domain.aggregate.Destination;
import com.mercadona.product.ean.domain.aggregate.Product;
import com.mercadona.product.ean.domain.aggregate.Provider;
import com.mercadona.product.ean.infrastructure.repository.dao.ProductDao;
import com.mercadona.product.ean.infrastructure.repository.entity.DestinationEntity;
import com.mercadona.product.ean.infrastructure.repository.entity.ProductEntity;
import com.mercadona.product.ean.infrastructure.repository.entity.ProviderEntity;
import com.mercadona.product.ean.infrastructure.repository.exception.ResourceAlreadyExistsException;
import com.mercadona.product.ean.infrastructure.repository.exception.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryImplTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductRepositoryImpl productRepository;

    @Captor
    private ArgumentCaptor<ProductEntity> productEntityCaptor;


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void givenExistingProduct_whenFindById_thenReturnsProduct() {

        Product productA = TestData.getProductA();

        when(productDao.findById(productA.getId())).thenReturn(Optional.of(TestData.getProductEntityA()));

        Product result = productRepository.findById(productA.getId());

        assertProductEquals(productA, result);
    }

    @Test
    public void givenNonExistingProduct_whenFindById_thenThrowsResourceNotFoundException() {

        Long productId = 1L;
        when(productDao.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            productRepository.findById(productId);
        });
    }

    @Test
    public void givenExistingProduct_whenFindByCodes_thenReturnsProduct() {
        ProductEntity productEntityA = TestData.getProductEntityA();
        String productCode = productEntityA.getProductCode();
        String destinationCode = productEntityA.getDestination().getCode();
        String providerCode = productEntityA.getProvider().getCode();

        when(productDao.findByCodes(productCode, providerCode, destinationCode)).thenReturn(Optional.of(productEntityA));

        Product result = productRepository.findByDetails(productCode, destinationCode, providerCode);

        assertProductEquals(TestData.getProductA(), result);
    }

    @Test
    public void givenExistingProduct_whenFindByCodes_thenThrowsResourceNotFoundException() {

        when(productDao.findByCodes(TestData.PRODUCT_CODE_A, TestData.PROVIDER_CODE_A, TestData.DESTINATION_CODE_A))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            productRepository.findByDetails(TestData.PRODUCT_CODE_A, TestData.DESTINATION_CODE_A, TestData.PROVIDER_CODE_A);
        });
    }

    @Test
    public void givenProductToSave_whenSaveCorrect_thenReturnProduct() {

        when(productDao.save(any(ProductEntity.class))).thenReturn(TestData.getProductEntityA());

        Product result = productRepository.save(TestData.getProductA());

        assertProductEquals(TestData.getProductA(), result);
    }

    @Test
    public void givenProductToSave_whenSaveCorrect_thenThrowsResourceAlreadyExistsException() {
        when(productDao.save(any(ProductEntity.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(ResourceAlreadyExistsException.class, () -> {
            productRepository.save(TestData.getProductA());
        });
    }

    @Test
    public void givenProductToUpdate_whenUpdateCorrect_thenReturnsUpdatedProduct() {
        ProductEntity productEntity = TestData.getProductEntityAWithUpdatedProductCode();
        Product productUpdated = TestData.getProductAWithUpdatedProductCode();
        when(productDao.update(any(ProductEntity.class))).thenReturn(productEntity);

        Product updatedProduct = productRepository.update(productUpdated);

        verify(productDao).update(productEntityCaptor.capture());
        assertEquals(productEntity.getProductCode(), productEntityCaptor.getValue().getProductCode());
        assertEquals(productEntity.getDestination().getCode(), productEntityCaptor.getValue().getDestination().getCode());
        assertEquals(productEntity.getProvider().getCode(), productEntityCaptor.getValue().getProvider().getCode());
        assertEquals(TestData.EAN_CODE_A, updatedProduct.getEanCode());
        assertEquals(TestData.UPDATED_PRODUCT_CODE_A, updatedProduct.getProductCode());
    }

    private void assertProductEquals(Product productA, Product productB) {
        assertEquals(productA.getId(), productB.getId());
        assertEquals(productA.getEanCode(), productB.getEanCode());
        assertEquals(productA.getProductCode(), productB.getProductCode());
        assertDestinationEquals(productA.getDestination(), productB.getDestination());
        assertProviderEquals(productA.getProvider(), productB.getProvider());
    }

    private void assertDestinationEquals(Destination destinationA, Destination destinationB) {
        assertEquals(destinationA.getId(), destinationB.getId());
        assertEquals(destinationA.getCode(), destinationB.getCode());
        assertEquals(destinationA.getName(), destinationB.getName());
    }

    private void assertProviderEquals(Provider providerA, Provider providerB) {
        assertEquals(providerA.getId(), providerB.getId());
        assertEquals(providerA.getCode(), providerB.getCode());
        assertEquals(providerA.getName(), providerB.getName());
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

        static ProductEntity getProductEntityAWithUpdatedProductCode() {
            ProviderEntity providerEntity = new ProviderEntity(PROVIDER_ID_A, PROVIDER_NAME_A, PROVIDER_CODE_A);
            DestinationEntity destinationEntity = new DestinationEntity(DESTINATION_ID_A, DESTINATION_NAME_A, DESTINATION_CODE_A);
            return new ProductEntity(PRODUCT_ID_A, EAN_CODE_A, UPDATED_PRODUCT_CODE_A, destinationEntity, providerEntity);
        }

        static Product getProductA() {
            Provider provider = new Provider(1, PROVIDER_CODE_A, PROVIDER_NAME_A);
            Destination destination = new Destination(1, DESTINATION_CODE_A, DESTINATION_NAME_A);
            return new Product(PRODUCT_ID_A, EAN_CODE_A, PRODUCT_CODE_A, provider, destination);
        }

        static Product getProductAWithUpdatedProductCode() {
            Provider provider = new Provider(1, PROVIDER_CODE_A, PROVIDER_NAME_A);
            Destination destination = new Destination(1, DESTINATION_CODE_A, DESTINATION_NAME_A);
            return new Product(PRODUCT_ID_A, EAN_CODE_A, UPDATED_PRODUCT_CODE_A, provider, destination);
        }

        static Product getProductAWithUnsavedDestinationAndProvider() {
            Provider provider = new Provider(null, PROVIDER_CODE_A, PROVIDER_NAME_A);
            Destination destination = new Destination(null, DESTINATION_CODE_A, DESTINATION_NAME_A);
            return new Product(PRODUCT_ID_A, EAN_CODE_A, PRODUCT_CODE_A, provider, destination);
        }

    }
}