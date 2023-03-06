package com.mercadona.product.ean.domain.factory;

import com.mercadona.product.ean.domain.aggregate.Destination;
import org.springframework.stereotype.Component;

@Component
public class DestinationFactory {

    public Destination buildDestinationFromEanCode(String eanCode) {
        return new Destination(eanCode.substring(12, 13));
    }
}
