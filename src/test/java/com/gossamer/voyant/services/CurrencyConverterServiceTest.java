package com.gossamer.voyant.services;

import com.gossamer.voyant.entities.ConversionRates;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CurrencyConverterServiceTest {
    @Autowired
    CurrencyConverterService currencyConverterService;


    @Test
    public void shouldHaveCurrenciesOnStartup() {
        List<ConversionRates> countriesList = currencyConverterService.getAllConversionRates();
        Assertions.assertEquals(4, countriesList.size());
    }

    @Test
    public void shouldHaveConversionRateForUSDtoGDP() {
        Assertions.assertEquals(0,
                BigDecimal.valueOf(0.72)
                        .compareTo(currencyConverterService.getConversionRate("USD", "GDP"))
                );
    }

    @Test
    public void shouldReturnNullIfNoConversionRateFound() {
        Assertions.assertNull(currencyConverterService.getConversionRate("USD","YEN"));
    }

    @Test(expected = ResponseStatusException.class)
    public void shouldThrowErrorIfCountryNotFound() {
        Assertions.assertNull(currencyConverterService.getConversionRate("USA","YEN"));
    }

}
