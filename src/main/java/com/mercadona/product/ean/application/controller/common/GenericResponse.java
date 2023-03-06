package com.mercadona.product.ean.application.controller.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenericResponse<T> implements Serializable {

    private T data;

    private List<Error> errors;

    public GenericResponse() {
        this.errors = new ArrayList<>();
    }

    public GenericResponse(T data) {
        this.data = data;
        this.errors = new ArrayList<>();
    }

    public static <T> GenericResponse<T> errorResponse(List<String> errorMessages) {
        GenericResponse<T> response = new GenericResponse<>();
        response.setErrors(errorMessages.stream().map(message -> new Error(message, ErrorLevel.ERROR)).toList());
        return response;
    }


    public static <T> GenericResponse<T> errorResponse(String ...message) {
        GenericResponse<T> response = new GenericResponse<>();
        Arrays.stream(message).forEach(error -> response.addError(new Error(error, ErrorLevel.ERROR)));
        return response;
    }

    public static <T> GenericResponse<T> warningResponse(String ...message) {
        GenericResponse<T> response = new GenericResponse<>();
        Arrays.stream(message).forEach(warning -> response.addError(new Error(warning, ErrorLevel.WARNING)));
        return response;
    }

    public static <T> GenericResponse<T> infoResponse(String ...message) {
        GenericResponse<T> response = new GenericResponse<>();
        Arrays.stream(message).forEach(info -> response.addError(new Error(info, ErrorLevel.WARNING)));
        return response;
    }

    public static <T> GenericResponse<T> dataResponse(T data) {
        return new GenericResponse<>(data);
    }

    public void addError(Error error) {
        errors.add(error);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }
}
