package com.mercadona.product.ean.application.controller.mapper;

import com.mercadona.product.ean.application.controller.dto.ProductResponseDTO;
import com.mercadona.product.ean.domain.aggregate.Product;

public class DtoMapper {

    public static ProductResponseDTO mapToProductResponseDTO(Product product) {
        return new ProductResponseDTO(product.getId(), product.getEanCode(), product.getProductCode(), product.getDestinationName(),
                product.getDestinationCode(), product.getProviderName(), product.getProviderCode(),
                !product.hasInconsistentEan() ? null : true);
    }
}
