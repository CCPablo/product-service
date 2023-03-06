package com.mercadona.product.ean.application.controller.common;

import java.io.Serializable;

public class Error implements Serializable {

    String message;

    ErrorLevel level;

    public Error(String message, ErrorLevel level) {
        this.message = message;
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ErrorLevel getLevel() {
        return level;
    }

    public void setLevel(ErrorLevel level) {
        this.level = level;
    }
}