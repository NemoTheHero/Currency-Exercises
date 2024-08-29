package com.gossamer.voyant.services;

import com.gossamer.voyant.entities.ConversionRates;
import com.gossamer.voyant.entities.Countries;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CurrencyConversionServiceTest {
    @Autowired
    CurrencyConversionService currencyConversionService;


    @Test
    public void shouldHaveCurrenciesOnStartup() {
        List<ConversionRates> countriesList = currencyConversionService.getAllConversionRates();
        Assertions.assertEquals(4, countriesList.size());
    }

}
