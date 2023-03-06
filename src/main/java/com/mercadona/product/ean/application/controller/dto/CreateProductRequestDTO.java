package com.mercadona.product.ean.application.controller.dto;

import com.mercadona.product.ean.application.controller.validator.ValidEan;

@ValidEan
public record CreateProductRequestDTO(String eanCode) {
}