package com.gossamer.voyant.controllers;

import com.gossamer.voyant.entities.Countries;
import com.gossamer.voyant.services.CountriesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CurrencyConverterController {
    private final CountriesService countriesService;

    public CurrencyConverterController(CountriesService countriesService) {
        this.countriesService = countriesService;
    }

    @GetMapping("/getAllCountries")
    List<Countries> getAllCountries() {

        return countriesService.getAllCountries() ;
    }
}
