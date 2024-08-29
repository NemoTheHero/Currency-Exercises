package com.gossamer.voyant.services;

import com.gossamer.voyant.entities.ConversionRates;
import com.gossamer.voyant.model.ConversionRatesWithCountryName;
import com.gossamer.voyant.model.CurrencyData;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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
        Assertions.assertNull(currencyConverterService.getConversionRate("USD", "YEN"));
    }

    @Test(expected = ResponseStatusException.class)
    public void shouldThrowErrorIfCountryNotFound() {
        Assertions.assertNull(currencyConverterService.getConversionRate("USA", "YEN"));
    }

    @Test
    public void shouldGetConversionRatesWithNames() {
        List<ConversionRatesWithCountryName> conversionRatesWithCountryNames =
                currencyConverterService.getAllConversionRatesWithCountryName();
        Assertions.assertEquals("USD", conversionRatesWithCountryNames.get(0).getOriginCountry());
        Assertions.assertEquals("GDP", conversionRatesWithCountryNames.get(0).getConversionCountry());
        Assertions.assertEquals(0,
                BigDecimal.valueOf(0.72)
                        .compareTo(conversionRatesWithCountryNames.get(0).getConversionRate())
        );

        Assertions.assertEquals("EUR", conversionRatesWithCountryNames.get(1).getOriginCountry());
        Assertions.assertEquals("GDP", conversionRatesWithCountryNames.get(1).getConversionCountry());
        Assertions.assertEquals(0,
                BigDecimal.valueOf(0.87)
                        .compareTo(conversionRatesWithCountryNames.get(1).getConversionRate())
        );

        Assertions.assertEquals("CAD", conversionRatesWithCountryNames.get(2).getOriginCountry());
        Assertions.assertEquals("USD", conversionRatesWithCountryNames.get(2).getConversionCountry());
        Assertions.assertEquals(0,
                BigDecimal.valueOf(0.78)
                        .compareTo(conversionRatesWithCountryNames.get(2).getConversionRate())
        );

        Assertions.assertEquals("YEN", conversionRatesWithCountryNames.get(3).getOriginCountry());
        Assertions.assertEquals("AUS", conversionRatesWithCountryNames.get(3).getConversionCountry());
        Assertions.assertEquals(0,
                BigDecimal.valueOf(0.012)
                        .compareTo(conversionRatesWithCountryNames.get(3).getConversionRate())
        );
    }

    @Test
    public void shouldBeAbleToAddMoreCurrencyRates() {
        List<List<String>> newCurrencyRates = Arrays.asList(
                Arrays.asList("YEN", "USD", ".01"),
                Arrays.asList("EUR", "YEN", "8.5")
                );
        CurrencyData currencyData = CurrencyData.builder()
                .currencyData(newCurrencyRates)
                .build();
        currencyConverterService.addNewCurrencyData(currencyData);

        Assertions.assertEquals(0,
                BigDecimal.valueOf(0.01)
                        .compareTo(currencyConverterService.getConversionRate("YEN", "USD"))
        );

        Assertions.assertEquals(0,
                BigDecimal.valueOf(8.5)
                        .compareTo(currencyConverterService.getConversionRate("EUR", "YEN"))
        );
    }


}
