package com.gossamer.voyant.entities;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import static jakarta.persistence.GenerationType.SEQUENCE;

public class CountriesEntity {
    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String country;
}
