package com.gossamer.voyant.controllers;

import com.gossamer.voyant.entities.ConversionRates;
import com.gossamer.voyant.model.ConversionRatesWithCountryName;
import com.gossamer.voyant.model.CurrencyData;
import com.gossamer.voyant.services.CountriesService;
import com.gossamer.voyant.services.CurrencyConversionService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    @GetMapping("/getAllConversionRatesWithCountryName")
    List<ConversionRatesWithCountryName> getAllConversionRatesWithCountryName() {

        return currencyConversionService.getAllConversionRatesWithCountryName() ;
    }

    @GetMapping("/getConversionRate")
    BigDecimal getConversionRate(@RequestParam("originCountry") String originCountry,
                                 @RequestParam("conversionCountry") String conversionCountry) {

        return currencyConversionService.getConversionRate(originCountry,conversionCountry) ;
    }

    @PostMapping("/addCurrencyConversions")
    void addCurrencyConversions(@RequestBody CurrencyData currencyData) {
        currencyConversionService.addNewCurrencyData(currencyData);
    }


}
