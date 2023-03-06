package com.mercadona.product.ean.infrastructure.repository.exception;

public class ResourceAlreadyExistsException extends RuntimeException {

    public ResourceAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
