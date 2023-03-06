package com.mercadona.product.ean.domain.aggregate;

public class Product {

    private Long id;

    private String eanCode;

    private String productCode;

    private Provider provider;

    private Destination destination;

    public Product(Long id, String eanCode, String productCode, Provider provider, Destination destination) {
        this.id = id;
        this.eanCode = eanCode;
        this.productCode = productCode;
        this.provider = provider;
        this.destination = destination;
    }

    public Product(String eanCode, String productCode, Provider provider, Destination destination) {
        this(null, eanCode, productCode, provider, destination);
    }

    public void setEanCode(String eanCode) {
        this.eanCode = eanCode;
        String providerCode = eanCode.substring(0, 7);
        if(!providerCode.equals(getProviderCode())) {
            this.provider = new Provider(providerCode);
        }
        this.productCode = eanCode.substring(7, 12);
        String destinationCode = eanCode.substring(12,13);
        if(!destinationCode.equals(getDestinationCode())) {
            this.destination = new Destination(destinationCode);
        }
    }

    /*
     ** Getters for mapping
     */

    public Long getId() {
        return id;
    }

    public String getEanCode() {
        return eanCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public Provider getProvider() {
        return provider;
    }

    public String getProviderName() {
        return provider != null ? provider.getName() : null;
    }

    public String getProviderCode() {
        return provider != null ? provider.getCode() : null;
    }

    public Destination getDestination() {
        return destination;
    }

    public String getDestinationName() {
        return destination != null ? destination.getName() : null;
    }

    public String getDestinationCode() {
        return destination != null ? destination.getCode() : null;
    }

    public boolean hasInconsistentEan() {
        return !getEanCode().equals(getProviderCode() + getProductCode() + getDestinationCode());
    }
}
