package com.mercadona.product.ean.infrastructure.repository.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(
        name = "product",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"product_code", "destination_id", "provider_id"},
                name = "uniqueEanConstraint"
        )
)
@NamedQueries({
        @NamedQuery(
                name = "findProductWithEanCode",
                query = "SELECT p FROM ProductEntity p WHERE p.eanCode = :eanCode"
        ),
        @NamedQuery(
                name = "findProductWithProductCode",
                query = "SELECT p FROM ProductEntity p WHERE p.productCode = :productCode"
        )
})
@NamedNativeQuery(
        name = "findProductWithCodes",
        query = "SELECT p.* FROM product p " +
                "JOIN provider pr ON p.provider_id = pr.id " +
                "JOIN destination d ON p.destination_id = d.id " +
                "WHERE pr.code = :providerCode " +
                "AND d.code = :destinationCode " +
                "AND p.product_code = :productCode",
        resultClass = ProductEntity.class
)
@DynamicInsert
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ean_code", nullable = false)
    @Size(min = 13, max = 13)
    private String eanCode;

    @Column(name = "product_code", nullable = false)
    @Size(min = 5, max = 5)
    private String productCode;

    @ManyToOne
    @JoinColumn(name = "destination_id", nullable = false)
    private DestinationEntity destination;

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private ProviderEntity provider;

    public ProductEntity() {
    }

    public ProductEntity(Long id, String eanCode, String productCode, DestinationEntity destination, ProviderEntity provider) {
        this.id = id;
        this.eanCode = eanCode;
        this.productCode = productCode;
        this.destination = destination;
        this.provider = provider;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEanCode() {
        return eanCode;
    }

    public void setEanCode(String eanCode) {
        this.eanCode = eanCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public DestinationEntity getDestination() {
        return destination;
    }

    public void setDestination(DestinationEntity destination) {
        this.destination = destination;
    }

    public ProviderEntity getProvider() {
        return provider;
    }

    public void setProvider(ProviderEntity provider) {
        this.provider = provider;
    }
}
