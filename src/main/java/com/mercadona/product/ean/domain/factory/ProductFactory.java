package com.mercadona.product.ean.domain.factory;

import com.mercadona.product.ean.domain.aggregate.Destination;
import com.mercadona.product.ean.domain.aggregate.Product;
import com.mercadona.product.ean.domain.aggregate.Provider;
import org.springframework.stereotype.Component;

@Component
public class ProductFactory {

    private final DestinationFactory destinationFactory;

    private final ProviderFactory providerFactory;

    public ProductFactory(DestinationFactory destinationFactory, ProviderFactory providerFactory) {
        this.destinationFactory = destinationFactory;
        this.providerFactory = providerFactory;
    }

    public Product getProductFromEanCode(String eanCode) {
        Destination destination = destinationFactory.buildDestinationFromEanCode(eanCode);
        Provider provider = providerFactory.buildProviderFromEanCode(eanCode);
        return new Product(eanCode, eanCode.substring(7, 12), provider, destination);
    }
}
