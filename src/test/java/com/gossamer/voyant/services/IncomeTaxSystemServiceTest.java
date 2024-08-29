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
        Assertions.assertEquals(4, incomeTaxBracketsList.size());


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

        Assertions.assertEquals(0, BigDecimal.valueOf(100000)
                .compareTo(incomeTaxBracketsList.get(3).getLowerLimit()));
        Assertions.assertEquals(0, BigDecimal.valueOf(100000000000000L)
                .compareTo(incomeTaxBracketsList.get(3).getHigherLimit()));
        Assertions.assertEquals(0, BigDecimal.valueOf(0.20)
                .compareTo(incomeTaxBracketsList.get(3).getTaxRate()));


    }
}
