package com.gossamer.voyant.controllers;

import com.gossamer.voyant.entities.Country;
import com.gossamer.voyant.services.CountriesService;
import com.gossamer.voyant.services.CurrencyConverterService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("countries")
public class CountriesController {


    private final CountriesService countriesService;

    public CountriesController(CountriesService countriesService, CurrencyConverterService currencyConverterService) {
        this.countriesService = countriesService;
    }

    @GetMapping("/getAllCountries")
    List<Country> getAllCountries() {

        return countriesService.getAllCountries() ;
    }

    @GetMapping("/getCountriesId")
    Long getCountriesId(@RequestParam String country) {

        return countriesService.getCountryId(country);
    }

    @PostMapping("/addCountry")
    void addCountry(@RequestBody Country country) {
        countriesService.addCountry(country.getCountry());
    }


}
