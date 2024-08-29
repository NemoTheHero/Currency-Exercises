package com.gossamer.voyant.dao;


import com.gossamer.voyant.entities.Countries;
import jakarta.persistence.Table;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Annotation
@Repository
@Table(name = "countries")
// Interface extending CrudRepository
public interface ICountriesDao
        extends CrudRepository<Countries, Long> {

    List<Countries> findByCountry(String country);
}
