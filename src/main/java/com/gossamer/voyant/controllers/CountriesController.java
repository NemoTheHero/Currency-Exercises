package com.gossamer.voyant.controllers;

import com.gossamer.voyant.entities.Countries;
import com.gossamer.voyant.services.CountriesService;
import com.gossamer.voyant.services.CurrencyConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("countries")
public class CountriesController {


    private final CountriesService countriesService;

    public CountriesController(CountriesService countriesService, CurrencyConversionService currencyConversionService) {
        this.countriesService = countriesService;
    }

    @GetMapping("/getAllCountries")
    List<Countries> getAllCountries() {

        return countriesService.getAllCountries() ;
    }

    @GetMapping("/getCountriesId/{country}")
    Long getCountriesId(@PathVariable String country) {

        return countriesService.getCountryId(country);
    }




}
