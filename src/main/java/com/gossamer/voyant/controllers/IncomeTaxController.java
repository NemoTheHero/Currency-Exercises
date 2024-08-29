package com.gossamer.voyant.controllers;

import com.gossamer.voyant.entities.ConversionRates;
import com.gossamer.voyant.entities.IncomeTaxBrackets;
import com.gossamer.voyant.services.CountriesService;
import com.gossamer.voyant.services.CurrencyConversionService;
import com.gossamer.voyant.services.IncomeTaxService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("incomeTax")
public class IncomeTaxController {

    private final IncomeTaxService incomeTaxService;

    public IncomeTaxController(IncomeTaxService incomeTaxService) {
        this.incomeTaxService = incomeTaxService;
    }

    @GetMapping("/getAllIncomeTaxes")
    List<IncomeTaxBrackets> getAllConversionRates() {

        return incomeTaxService.getAllTaxBrackets() ;
    }

    @GetMapping("/getIncomeTaxBracketsForIncome")
    List<IncomeTaxBrackets> getIncomeTaxBracketsForIncome(@RequestParam BigDecimal income) {

        return incomeTaxService.getIncomeBracketsForIncome(income) ;
    }
}
