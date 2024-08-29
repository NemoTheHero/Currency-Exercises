package com.gossamer.voyant.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.math.BigDecimal;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity

public class CurrencyConversion {
    @Id
    @GeneratedValue(strategy = IDENTITY)
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

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getConversionCountry() {
        return conversionCountry;
    }

    public void setConversionCountry(String conversionCountry) {
        this.conversionCountry = conversionCountry;
    }

    public BigDecimal getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(BigDecimal conversionRate) {
        this.conversionRate = conversionRate;
    }
}
