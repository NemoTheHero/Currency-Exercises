package com.gossamer.voyant.controllers;

import com.gossamer.voyant.entities.IncomeTaxBrackets;
import com.gossamer.voyant.services.IncomeTaxSystemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("incomeTax")
public class IncomeTaxSystemController {

    private final IncomeTaxSystemService incomeTaxSystemService;

    public IncomeTaxSystemController(IncomeTaxSystemService incomeTaxSystemService) {
        this.incomeTaxSystemService = incomeTaxSystemService;
    }

    @GetMapping("/getAllIncomeTaxesBrackets")
    List<IncomeTaxBrackets> getAllIncomeTaxBrackets() {
        return incomeTaxSystemService.getAllTaxBrackets() ;
    }

    @GetMapping("/getIncomeTaxBracketsForIncome")
    List<IncomeTaxBrackets> getIncomeTaxBracketsForIncome(@RequestParam BigDecimal income) {
        return incomeTaxSystemService.getIncomeBracketsForIncomeLowToHigh(income) ;
    }

    @GetMapping("/calculateIncomeTax")
    BigDecimal calculateIncomeTax(@RequestParam BigDecimal income) {

        return incomeTaxSystemService.calculateIncomeTax(income) ;
    }

    @GetMapping("/determineMarginalTaxRate")
    BigDecimal determineMarginalTaxRate(@RequestParam BigDecimal income) {

        return incomeTaxSystemService.determineMarginalTaxRate(income) ;
    }

    @GetMapping("/determineEffectiveTaxRate")
    BigDecimal determineEffectiveTaxRate(@RequestParam BigDecimal income) {

        return incomeTaxSystemService.determineEffectiveTaxRate(income) ;
    }

}
