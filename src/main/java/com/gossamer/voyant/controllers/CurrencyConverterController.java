package com.gossamer.voyant.controllers;

import com.gossamer.voyant.entities.ConversionRates;
import com.gossamer.voyant.model.ConversionRatesWithCountryName;
import com.gossamer.voyant.model.CurrencyData;
import com.gossamer.voyant.services.CurrencyConverterService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("currencyConvertor")
public class CurrencyConverterController {
    private final CurrencyConverterService currencyConverterService;

    public CurrencyConverterController(CurrencyConverterService currencyConverterService) {
        this.currencyConverterService = currencyConverterService;
    }



    @GetMapping("/getAllConversionRates")
    List<ConversionRates> getAllConversionRates() {

        return currencyConverterService.getAllConversionRates() ;
    }

    @GetMapping("/getAllConversionRatesWithCountryName")
    List<ConversionRatesWithCountryName> getAllConversionRatesWithCountryName() {

        return currencyConverterService.getAllConversionRatesWithCountryName() ;
    }

    @GetMapping("/getConversionRate")
    BigDecimal getConversionRate(@RequestParam("originCountry") String originCountry,
                                 @RequestParam("conversionCountry") String conversionCountry) {

        return currencyConverterService.getConversionRate(originCountry,conversionCountry) ;
    }

    @PostMapping("/addCurrencyConversions")
    void addCurrencyConversions(@RequestBody CurrencyData currencyData) {
        currencyConverterService.addNewCurrencyData(currencyData);
    }


}
