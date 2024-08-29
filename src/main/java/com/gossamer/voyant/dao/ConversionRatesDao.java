package com.gossamer.voyant.dao;



import com.gossamer.voyant.entities.ConversionRates;
import jakarta.persistence.Table;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Annotation
@Repository
@Table(name = "conversion_rates")
// Interface extending CrudRepository
public interface ConversionRatesDao
        extends CrudRepository<ConversionRates, Long> {

    List<ConversionRates> findByOriginCountryFidAndConversionCountryFid(Long originCountryFid, Long conversionCountryFid);



}

