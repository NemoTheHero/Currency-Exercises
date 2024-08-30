package com.gossamer.voyant.services;

import com.gossamer.voyant.entities.ConversionRates;
import com.gossamer.voyant.entities.IncomeTaxBrackets;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class IncomeTaxSystemServiceTest {

    @Autowired
    IncomeTaxSystemService incomeTaxSystemService;

    @Test
    public void shouldHaveTaxBracketsDataOnStartup() {
        List<IncomeTaxBrackets> incomeTaxBracketsList = incomeTaxSystemService.getAllTaxBrackets();
        Assertions.assertEquals(5, incomeTaxBracketsList.size());

        Assertions.assertEquals(0, BigDecimal.valueOf(0)
                .compareTo(incomeTaxBracketsList.get(0).getLowerLimit()));
        Assertions.assertEquals(0, BigDecimal.valueOf(20000)
                .compareTo(incomeTaxBracketsList.get(0).getHigherLimit()));
        Assertions.assertEquals(0, BigDecimal.valueOf(0.10)
                .compareTo(incomeTaxBracketsList.get(0).getTaxRate()));

        Assertions.assertEquals(0, BigDecimal.valueOf(20000)
                .compareTo(incomeTaxBracketsList.get(1).getLowerLimit()));
        Assertions.assertEquals(0, BigDecimal.valueOf(50000)
                .compareTo(incomeTaxBracketsList.get(1).getHigherLimit()));
        Assertions.assertEquals(0, BigDecimal.valueOf(0.15)
                .compareTo(incomeTaxBracketsList.get(1).getTaxRate()));

        Assertions.assertEquals(0, BigDecimal.valueOf(50000)
                .compareTo(incomeTaxBracketsList.get(2).getLowerLimit()));
        Assertions.assertEquals(0, BigDecimal.valueOf(100000)
                .compareTo(incomeTaxBracketsList.get(2).getHigherLimit()));
        Assertions.assertEquals(0, BigDecimal.valueOf(0.20)
                .compareTo(incomeTaxBracketsList.get(2).getTaxRate()));

        Assertions.assertEquals(0, BigDecimal.valueOf(150000)
                .compareTo(incomeTaxBracketsList.get(3).getLowerLimit()));
        Assertions.assertEquals(0, BigDecimal.valueOf(100000000000000L)
                .compareTo(incomeTaxBracketsList.get(3).getHigherLimit()));
        Assertions.assertEquals(0, BigDecimal.valueOf(0.30)
                .compareTo(incomeTaxBracketsList.get(3).getTaxRate()));

        Assertions.assertEquals(0, BigDecimal.valueOf(100000)
                .compareTo(incomeTaxBracketsList.get(4).getLowerLimit()));
        Assertions.assertEquals(0, BigDecimal.valueOf(150000)
                .compareTo(incomeTaxBracketsList.get(4).getHigherLimit()));
        Assertions.assertEquals(0, BigDecimal.valueOf(0.25)
                .compareTo(incomeTaxBracketsList.get(4).getTaxRate()));
    }

    @Test
    public void bracketShouldBeOrderedLowToHighBasedOnIncome() {
        List<IncomeTaxBrackets> ascendingIncomeTaxBrackets =
                incomeTaxSystemService.getIncomeBracketsForIncomeLowToHigh(BigDecimal.valueOf(120000L));

        Assertions.assertEquals(0, BigDecimal.valueOf(0)
                .compareTo(ascendingIncomeTaxBrackets.get(0).getLowerLimit()));
        Assertions.assertEquals(0, BigDecimal.valueOf(20000)
                .compareTo(ascendingIncomeTaxBrackets.get(0).getHigherLimit()));
        Assertions.assertEquals(0, BigDecimal.valueOf(0.10)
                .compareTo(ascendingIncomeTaxBrackets.get(0).getTaxRate()));

        Assertions.assertEquals(0, BigDecimal.valueOf(20000)
                .compareTo(ascendingIncomeTaxBrackets.get(1).getLowerLimit()));
        Assertions.assertEquals(0, BigDecimal.valueOf(50000)
                .compareTo(ascendingIncomeTaxBrackets.get(1).getHigherLimit()));
        Assertions.assertEquals(0, BigDecimal.valueOf(0.15)
                .compareTo(ascendingIncomeTaxBrackets.get(1).getTaxRate()));

        Assertions.assertEquals(0, BigDecimal.valueOf(50000)
                .compareTo(ascendingIncomeTaxBrackets.get(2).getLowerLimit()));
        Assertions.assertEquals(0, BigDecimal.valueOf(100000)
                .compareTo(ascendingIncomeTaxBrackets.get(2).getHigherLimit()));
        Assertions.assertEquals(0, BigDecimal.valueOf(0.20)
                .compareTo(ascendingIncomeTaxBrackets.get(2).getTaxRate()));

        Assertions.assertEquals(0, BigDecimal.valueOf(100000)
                .compareTo(ascendingIncomeTaxBrackets.get(3).getLowerLimit()));
        Assertions.assertEquals(0, BigDecimal.valueOf(150000)
                .compareTo(ascendingIncomeTaxBrackets.get(3).getHigherLimit()));
        Assertions.assertEquals(0, BigDecimal.valueOf(0.25)
                .compareTo(ascendingIncomeTaxBrackets.get(3).getTaxRate()));
    }

    @Test
    public void shouldCalculateIncomeTaxCorrectly() {
        //return 0 if null
        Assertions.assertEquals(0, BigDecimal.valueOf(0)
                .compareTo(incomeTaxSystemService.calculateIncomeTax(null)));

        //return 0 if no income
        Assertions.assertEquals(0, BigDecimal.valueOf(0)
                .compareTo(incomeTaxSystemService.calculateIncomeTax(BigDecimal.valueOf(0))));

        // $15000 taxed at 10% = 1500
        Assertions.assertEquals(0, BigDecimal.valueOf(1500)
                .compareTo(incomeTaxSystemService.calculateIncomeTax(BigDecimal.valueOf(15000))));
        // $20,000 taxed at 10% = 2000
        Assertions.assertEquals(0, BigDecimal.valueOf(2000)
                .compareTo(incomeTaxSystemService.calculateIncomeTax(BigDecimal.valueOf(20000))));
        // $30,000
        // $20,000 taxed at 10% = 2000 +
        // $10,000 taxed at 15% = 1500
        Assertions.assertEquals(0, BigDecimal.valueOf(3500)
                .compareTo(incomeTaxSystemService.calculateIncomeTax(BigDecimal.valueOf(30000))));
        // $20,001
        // $20,000 taxed at 10% = 2000 +
        // $1 taxed at 15% = .15
        Assertions.assertEquals(0, BigDecimal.valueOf(2000.15)
                .compareTo(incomeTaxSystemService.calculateIncomeTax(BigDecimal.valueOf(20001))));
        // $100,000.10
        // $20,000 taxed at 10% = 2000 +
        // $30,000 taxed at 15% = 4500 +
        // $50,000 taxed at 20% = 10000
        // $0.10 taxed at 25% = .025
        Assertions.assertEquals(0, BigDecimal.valueOf(16500.025)
                .compareTo(incomeTaxSystemService.calculateIncomeTax(BigDecimal.valueOf(100000.10))));
    }

    @Test
    public void shouldDetermineEffectiveTaxRateCorrectly() {
        //return 0 if null
        Assertions.assertEquals(0, BigDecimal.valueOf(0)
                .compareTo(incomeTaxSystemService.determineEffectiveTaxRate(null)));

        //return 0 if no income
        Assertions.assertEquals(0, BigDecimal.valueOf(0)
                .compareTo(incomeTaxSystemService.determineEffectiveTaxRate(BigDecimal.valueOf(0))));

        // $10000 taxed at 10% is 1000, 1000/10000
        Assertions.assertEquals(0, BigDecimal.valueOf(.10)
                .compareTo(incomeTaxSystemService.determineEffectiveTaxRate(BigDecimal.valueOf(10000))));

        // $30,000
        // $20,000 taxed at 10% = 2000 +
        // $10,000 taxed at 15% = 1500
        // 3500/30000 = 0.116666666667 but should clamp down to 7 decimal places
        Assertions.assertEquals(0, BigDecimal.valueOf(.1166667)
                .compareTo(incomeTaxSystemService.determineEffectiveTaxRate(BigDecimal.valueOf(30000))));
    }

    @Test
    public void shouldDetermineMarginTaxCorrectly() {
        //return 0 if null
        Assertions.assertEquals(0, BigDecimal.valueOf(0)
                .compareTo(incomeTaxSystemService.determineMarginalTaxRate(null)));

        //return 0 if no income
        Assertions.assertEquals(0, BigDecimal.valueOf(0)
                .compareTo(incomeTaxSystemService.determineMarginalTaxRate(BigDecimal.valueOf(0))));

        // tax range 0-20000 is 10%
        Assertions.assertEquals(0, BigDecimal.valueOf(.10)
                .compareTo(incomeTaxSystemService.determineMarginalTaxRate(BigDecimal.valueOf(10000))));

        // tax range 0-20000 is 10%, 20000-50000 is 15%, if on the fence use lower tax bracket
        Assertions.assertEquals(0, BigDecimal.valueOf(.10)
                .compareTo(incomeTaxSystemService.determineMarginalTaxRate(BigDecimal.valueOf(20000))));


        Assertions.assertEquals(0, BigDecimal.valueOf(0.15)
                .compareTo(incomeTaxSystemService.determineMarginalTaxRate(BigDecimal.valueOf(20000.01))));

        Assertions.assertEquals(0, BigDecimal.valueOf(0.30)
                .compareTo(incomeTaxSystemService.determineMarginalTaxRate(BigDecimal.valueOf(300000))));
    }

}
