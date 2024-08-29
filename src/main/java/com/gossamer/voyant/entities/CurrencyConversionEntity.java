package com.gossamer.voyant.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.math.BigDecimal;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
public class CurrencyConversionEntity {
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String originCountry;

    @Column(nullable = false)
    private String conversionCountry;

    @Column(nullable = false)
    private BigDecimal conversionRate;





    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
