package com.mercadona.product.ean.infrastructure.repository.mapper;

import com.mercadona.product.ean.domain.aggregate.Destination;
import com.mercadona.product.ean.domain.aggregate.Product;
import com.mercadona.product.ean.domain.aggregate.Provider;
import com.mercadona.product.ean.infrastructure.repository.entity.DestinationEntity;
import com.mercadona.product.ean.infrastructure.repository.entity.ProductEntity;
import com.mercadona.product.ean.infrastructure.repository.entity.ProviderEntity;

public class AggregateMapper {

    public static Product productEntityToProductAggregate(ProductEntity productEntity) {
        Destination destination = destinationEntityToAggregate(productEntity.getDestination());
        Provider provider = providerEntityToAggregate(productEntity.getProvider());
        return new Product(productEntity.getId(), productEntity.getEanCode(), productEntity.getProductCode(), provider, destination);
    }


    public static Destination destinationEntityToAggregate(DestinationEntity destinationEntity) {
        return new Destination(destinationEntity.getId(), destinationEntity.getCode(), destinationEntity.getName());
    }

    public static Provider providerEntityToAggregate(ProviderEntity providerEntity) {
        return new Provider(providerEntity.getId(), providerEntity.getCode(), providerEntity.getName());
    }

    public static ProductEntity productAggregateToProductEntity(Product product) {
        DestinationEntity destinationEntity = destinationAggregateToDestinationEntity(product.getDestination());
        ProviderEntity providerEntity = providerAggregateToProviderEntity(product.getProvider());
        return new ProductEntity(product.getId(), product.getEanCode(), product.getProductCode(), destinationEntity, providerEntity);
    }

    public static DestinationEntity destinationAggregateToDestinationEntity(Destination destination) {
        return new DestinationEntity(destination.getId(), destination.getName(), destination.getCode());
    }

    public static ProviderEntity providerAggregateToProviderEntity(Provider provider) {
        return new ProviderEntity(provider.getId(), provider.getName(), provider.getCode());
    }
}
