package com.gossamer.voyant.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ConversionRates {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long originCountryFid;

    @Column(nullable = false)
    private Long conversionCountryFid;

    @Column(nullable = false)
    private BigDecimal conversionRate;
}
