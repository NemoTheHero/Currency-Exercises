package com.gossamer.voyant.controllers;

import com.gossamer.voyant.entities.ConversionRates;
import com.gossamer.voyant.entities.Countries;
import com.gossamer.voyant.services.CountriesService;
import com.gossamer.voyant.services.CurrencyConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("currencyConvertor")
public class CurrencyConverterController {
    private final CountriesService countriesService;
    private final CurrencyConversionService currencyConversionService;

    public CurrencyConverterController(CountriesService countriesService, CurrencyConversionService currencyConversionService) {
        this.countriesService = countriesService;
        this.currencyConversionService = currencyConversionService;
    }



    @GetMapping("/getAllConversionRates")
    List<ConversionRates> getAllConversionRates() {

        return currencyConversionService.getAllConversionRates() ;
    }


}
