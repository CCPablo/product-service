package com.mercadona.product.ean.infrastructure.repository.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(
        name = "destination",
        uniqueConstraints = @UniqueConstraint(columnNames = {"code"})
)
@NamedQuery(
        name = "findDestinationWithCode",
        query = "SELECT c FROM DestinationEntity c WHERE c.code = :code"
)
@DynamicInsert
public class DestinationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name", columnDefinition = "varchar(52) default 'Not set'", nullable = false)
    @Size(max = 52)
    private String name;

    @Column(name = "code", nullable = false)
    @Size(min = 1, max = 1)
    private String code;

    public DestinationEntity() {
    }

    public DestinationEntity(Integer id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public DestinationEntity(String code) {
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
