package com.gossamer.voyant.services;

import com.gossamer.voyant.dao.ICountriesDao;
import com.gossamer.voyant.entities.Countries;

import org.springframework.stereotype.Service;

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
}
