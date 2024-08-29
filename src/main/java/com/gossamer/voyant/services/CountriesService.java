package com.gossamer.voyant.services;

import com.gossamer.voyant.dao.ICountriesDao;
import com.gossamer.voyant.entities.Countries;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CountriesService {

    private final ICountriesDao countriesDao;

    public CountriesService(ICountriesDao countriesDao) {
        this.countriesDao = countriesDao;
    }


    public List<Countries> getAllCountries() {
        return (List<Countries>) countriesDao.findAll();
    }

    public Long getCountryId(String country) {
        List<Countries> countryId = countriesDao.findByCountry(country);
        if (countryId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    country + " not found");
        }
        return countryId.get(0).getId();
    }
}
