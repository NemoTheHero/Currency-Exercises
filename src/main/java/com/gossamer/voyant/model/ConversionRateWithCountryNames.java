package com.gossamer.voyant.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder
public class ConversionRateWithCountryNames {
    private String originCountry;

    private String conversionCountry;

    private BigDecimal conversionRate;
}
