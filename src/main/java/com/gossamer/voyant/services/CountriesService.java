package com.gossamer.voyant.services;

import com.gossamer.voyant.dao.CountriesDao;
import com.gossamer.voyant.entities.Country;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CountriesService {

    private final CountriesDao countriesDao;

    public CountriesService(CountriesDao countriesDao) {
        this.countriesDao = countriesDao;
    }


    public List<Country> getAllCountries() {
        return (List<Country>) countriesDao.findAll();
    }

    public Long getCountryId(String country) {
        List<Country> countryId = countriesDao.findByCountry(country);
        if (countryId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    country + " not found");
        }
        return countryId.get(0).getId();
    }


    public void addCountry(String countryName) {
        countriesDao.save(Country.builder().country(countryName).build());
    }

    public Map<Long,String> countriesToMap () {
        return getAllCountries().stream()
                .collect(Collectors.toMap(Country::getId, Country::getCountry));
    }

    public Map<Long,String> countriesIdToMap () {
        return getAllCountries().stream()
                .collect(Collectors.toMap(Country::getId, Country::getCountry));
    }
}
