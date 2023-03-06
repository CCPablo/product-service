package com.mercadona.product.ean.infrastructure.repository.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(
        name = "provider",
        uniqueConstraints = @UniqueConstraint(columnNames = {"code"})
)
@NamedQuery(
        name = "findProviderWithCode",
        query = "SELECT p FROM ProviderEntity p WHERE p.code = :code"
)
@DynamicInsert
public class ProviderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name", columnDefinition = "varchar(52) default 'Not Set'", nullable = false)
    @Size(max = 52)
    private String name;

    @Column(name = "code", nullable = false)
    @Size(min = 7, max = 7)
    private String code;

    public ProviderEntity() {
    }

    public ProviderEntity(Integer id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public ProviderEntity(String code) {
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
