package com.mercadona.product.ean.domain.aggregate;

public class Destination {

    private Integer id;

    private String code;

    private String name;

    public Destination(Integer id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public Destination(String code) {
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
