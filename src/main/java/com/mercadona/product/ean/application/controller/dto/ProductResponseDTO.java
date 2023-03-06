package com.mercadona.product.ean.application.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProductResponseDTO(Long id, String eanCode, String productCode, String destinationName,
                                 String destinationCode, String providerName, String providerCode, Boolean inconsistentEan) {

}