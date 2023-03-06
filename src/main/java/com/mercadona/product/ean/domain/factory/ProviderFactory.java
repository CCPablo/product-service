package com.mercadona.product.ean.domain.factory;


import com.mercadona.product.ean.domain.aggregate.Provider;
import org.springframework.stereotype.Component;

@Component
public class ProviderFactory {

    public Provider buildProviderFromEanCode(String eanCode) {
        return new Provider(eanCode.substring(0, 7));
    }
}
