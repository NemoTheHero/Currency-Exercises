package com.gossamer.voyant.services;

import com.gossamer.voyant.entities.ConversionRates;
import com.gossamer.voyant.model.ConversionRatesWithCountryName;
import com.gossamer.voyant.model.CurrencyData;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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

    @Autowired
    CountriesService countriesService;


    @Test
    public void shouldHaveCurrenciesOnStartup() {
        List<ConversionRates> countriesList = currencyConverterService.getAllConversionRates();
        Assertions.assertEquals(8, countriesList.size());
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

    @Test
    public void shouldReturnInverseIfConversionRateCanBeMirroredFound() {
        Assertions.assertEquals(0,
                BigDecimal.valueOf(1.38889)
                        .compareTo(currencyConverterService.getConversionRate("GDP", "USD")));
    }

    @Test
    public void shouldDetermineConversionRateByRelationship() {
        Assertions.assertEquals(0,
                BigDecimal.valueOf(.5616)
                        .compareTo(currencyConverterService.getConversionRate("CAD", "GDP"))
        );

        Assertions.assertEquals(0,
                BigDecimal.valueOf(.9)
                        .compareTo(currencyConverterService.getConversionRate("USD", "MEX"))
        );

        Assertions.assertEquals(0,
                BigDecimal.valueOf(1.8)
                        .compareTo(currencyConverterService.getConversionRate("USD", "CHF"))
        );

        Assertions.assertEquals(0,
                BigDecimal.valueOf(.45)
                        .compareTo(currencyConverterService.getConversionRate("USD", "INR"))
        );
        Assertions.assertNull(currencyConverterService.getConversionRate("INR", "YEN"));
    }

    @Test
    public void shouldAddNewConversionRatesWhenDeterminingConversionRateRelationships() {

        Assertions.assertEquals(8, currencyConverterService.getAllConversionRates().size());
        Assertions.assertEquals(2,
                currencyConverterService.getConversionRatesForCountry(countriesService.getCountryId("USD")).size());
        Assertions.assertEquals(0,
                currencyConverterService.getConversionRatesForCountry(countriesService.getCountryId("GDP")).size());
        Assertions.assertEquals(1,
                currencyConverterService.getConversionRatesForCountry(countriesService.getCountryId("MEX")).size());

        currencyConverterService.getConversionRate("USD", "MEX");


        Assertions.assertEquals(12,
                currencyConverterService.getAllConversionRates().size());
        Assertions.assertEquals(3,
                currencyConverterService.getConversionRatesForCountry(countriesService.getCountryId("USD")).size());
        Assertions.assertEquals(2,
                currencyConverterService.getConversionRatesForCountry(countriesService.getCountryId("GDP")).size());
        Assertions.assertEquals(2,
                currencyConverterService.getConversionRatesForCountry(countriesService.getCountryId("MEX")).size());
    }

    @Test
    public void shouldAddNewConversionRatesWhenDeterminingConversionRateRelationships2() {

        Assertions.assertEquals(8, currencyConverterService.getAllConversionRates().size());
        Assertions.assertEquals(2,
                currencyConverterService.getConversionRatesForCountry(countriesService.getCountryId("USD")).size());
        Assertions.assertEquals(0,
                currencyConverterService.getConversionRatesForCountry(countriesService.getCountryId("GDP")).size());
        Assertions.assertEquals(1,
                currencyConverterService.getConversionRatesForCountry(countriesService.getCountryId("MEX")).size());
        Assertions.assertEquals(1,
                currencyConverterService.getConversionRatesForCountry(countriesService.getCountryId("CHF")).size());

        currencyConverterService.getConversionRate("CHF", "USD");


        Assertions.assertEquals(14,
                currencyConverterService.getAllConversionRates().size());
        Assertions.assertEquals(3,
                currencyConverterService.getConversionRatesForCountry(countriesService.getCountryId("USD")).size());
        Assertions.assertEquals(2,
                currencyConverterService.getConversionRatesForCountry(countriesService.getCountryId("GDP")).size());
        Assertions.assertEquals(2,
                currencyConverterService.getConversionRatesForCountry(countriesService.getCountryId("MEX")).size());
        Assertions.assertEquals(3,
                currencyConverterService.getConversionRatesForCountry(countriesService.getCountryId("CHF")).size());
    }
    @Test
    public void getConversionRateShouldCreateDirectConversionsToNodesAlongPath() {
        Assertions.assertTrue(currencyConverterService.getRateByOriginAndConversionCurrency(
                countriesService.getCountryId("MEX"),
                countriesService.getCountryId("CHF")).isEmpty());
        Assertions.assertTrue(currencyConverterService.getRateByOriginAndConversionCurrency(
                countriesService.getCountryId("GDP"),
                countriesService.getCountryId("CHF")).isEmpty());
        Assertions.assertTrue(currencyConverterService.getRateByOriginAndConversionCurrency(
                countriesService.getCountryId("USD"),
                countriesService.getCountryId("CHF")).isEmpty());
        Assertions.assertTrue(currencyConverterService.getRateByOriginAndConversionCurrency(
                countriesService.getCountryId("CHF"),
                countriesService.getCountryId("GDP")).isEmpty());
        Assertions.assertTrue(currencyConverterService.getRateByOriginAndConversionCurrency(
                countriesService.getCountryId("CHF"),
                countriesService.getCountryId("USD")).isEmpty());

        Assertions.assertFalse(currencyConverterService.getRateByOriginAndConversionCurrency(
                countriesService.getCountryId("CHF"),
                countriesService.getCountryId("MEX")).isEmpty());
        Assertions.assertFalse(currencyConverterService.getRateByOriginAndConversionCurrency(
                countriesService.getCountryId("MEX"),
                countriesService.getCountryId("GDP")).isEmpty());
        Assertions.assertFalse(currencyConverterService.getRateByOriginAndConversionCurrency(
                countriesService.getCountryId("USD"),
                countriesService.getCountryId("GDP")).isEmpty());


        currencyConverterService.getConversionRate("CHF", "USD");

        Assertions.assertFalse(currencyConverterService.getRateByOriginAndConversionCurrency(
                countriesService.getCountryId("MEX"),
                countriesService.getCountryId("CHF")).isEmpty());
        Assertions.assertFalse(currencyConverterService.getRateByOriginAndConversionCurrency(
                countriesService.getCountryId("GDP"),
                countriesService.getCountryId("CHF")).isEmpty());
        Assertions.assertFalse(currencyConverterService.getRateByOriginAndConversionCurrency(
                countriesService.getCountryId("USD"),
                countriesService.getCountryId("CHF")).isEmpty());
        Assertions.assertFalse(currencyConverterService.getRateByOriginAndConversionCurrency(
                countriesService.getCountryId("CHF"),
                countriesService.getCountryId("GDP")).isEmpty());
        Assertions.assertFalse(currencyConverterService.getRateByOriginAndConversionCurrency(
                countriesService.getCountryId("CHF"),
                countriesService.getCountryId("USD")).isEmpty());

        Assertions.assertFalse(currencyConverterService.getRateByOriginAndConversionCurrency(
                countriesService.getCountryId("CHF"),
                countriesService.getCountryId("MEX")).isEmpty());
        Assertions.assertFalse(currencyConverterService.getRateByOriginAndConversionCurrency(
                countriesService.getCountryId("MEX"),
                countriesService.getCountryId("GDP")).isEmpty());
        Assertions.assertFalse(currencyConverterService.getRateByOriginAndConversionCurrency(
                countriesService.getCountryId("USD"),
                countriesService.getCountryId("GDP")).isEmpty());

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
        currencyConverterService.updateCurrencyData(currencyData);

        Assertions.assertEquals(0,
                BigDecimal.valueOf(0.01)
                        .compareTo(currencyConverterService.getConversionRate("YEN", "USD"))
        );

        Assertions.assertEquals(0,
                BigDecimal.valueOf(8.5)
                        .compareTo(currencyConverterService.getConversionRate("EUR", "YEN"))
        );
    }

    @Test
    public void addNewCurrencyDataShouldUpdateIfRateAlreadyExists() {
        List<ConversionRatesWithCountryName> conversionRatesWithCountryNames =
                currencyConverterService.getAllConversionRatesWithCountryName();
        Assertions.assertEquals("USD", conversionRatesWithCountryNames.get(0).getOriginCountry());
        Assertions.assertEquals("GDP", conversionRatesWithCountryNames.get(0).getConversionCountry());
        Assertions.assertEquals(0,
                BigDecimal.valueOf(0.72) //current conversion rate
                        .compareTo(conversionRatesWithCountryNames.get(0).getConversionRate())
        );

        Assertions.assertEquals("CAD", conversionRatesWithCountryNames.get(2).getOriginCountry());
        Assertions.assertEquals("USD", conversionRatesWithCountryNames.get(2).getConversionCountry());
        Assertions.assertEquals(0,
                BigDecimal.valueOf(0.78) //current conversion rate
                        .compareTo(conversionRatesWithCountryNames.get(2).getConversionRate())
        );

        List<List<String>> newCurrencyRates = Arrays.asList(
                Arrays.asList("USD", "GDP", ".75"),
                Arrays.asList("CAD", "USD", ".90")
        );

        CurrencyData currencyData = CurrencyData.builder()
                .currencyData(newCurrencyRates)
                .build();
        currencyConverterService.updateCurrencyData(currencyData);

        List<ConversionRatesWithCountryName> updatedConversationRates =
                currencyConverterService.getAllConversionRatesWithCountryName();

        Assertions.assertEquals("USD", updatedConversationRates.get(0).getOriginCountry());
        Assertions.assertEquals("GDP", updatedConversationRates.get(0).getConversionCountry());
        Assertions.assertEquals(0,
                BigDecimal.valueOf(0.75) // new value is .75
                        .compareTo(updatedConversationRates.get(0).getConversionRate())
        );

        Assertions.assertEquals("CAD", updatedConversationRates.get(2).getOriginCountry());
        Assertions.assertEquals("USD", updatedConversationRates.get(2).getConversionCountry());
        Assertions.assertEquals(0,
                BigDecimal.valueOf(0.90) // new value is .90
                        .compareTo(updatedConversationRates.get(2).getConversionRate())
        );

    }

    @Test
    public void addNewCurrencyDataShouldThrowIfCountryIsNotInSystem() {
        //test originCountry
        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> {
                    List<List<String>> newCurrencyRates = List.of(
                            Arrays.asList("USA", "USD", ".01")
                    );
                    currencyConverterService.updateCurrencyData(CurrencyData.builder()
                            .currencyData(newCurrencyRates)
                            .build());
                }
        );

        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        Assertions.assertEquals(String.format("404 NOT_FOUND \"Country not in system - %s\"", "USA"), exception.getMessage());

        //test conversionCountry
        exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> {
                    List<List<String>> newCurrencyRates = List.of(
                            Arrays.asList("YEN", "USA", ".01")
                    );
                    currencyConverterService.updateCurrencyData(CurrencyData.builder()
                            .currencyData(newCurrencyRates)
                            .build());
                }
        );

        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        Assertions.assertEquals(String.format("404 NOT_FOUND \"Country not in system - %s\"", "USA"), exception.getMessage());
    }

    @Test
    public void shouldThrowIfUnableToParseNewRate() {
        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> {
                    List<List<String>> newCurrencyRates = List.of(
                            Arrays.asList("YEN", "USD", "0.22O")
                    );
                    currencyConverterService.updateCurrencyData(CurrencyData.builder()
                            .currencyData(newCurrencyRates)
                            .build());
                }
        );

        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatusCode());
        Assertions.assertEquals(
                String.format("422 UNPROCESSABLE_ENTITY \"Problem with - %s to %s , %s\"", "YEN", "USD", "0.22O"),
                exception.getMessage());
    }

    @Test
    public void shouldInverseExchangeRate() {
        Assertions.assertEquals(0,
                currencyConverterService.reverseConversion(BigDecimal.valueOf(0)).compareTo(BigDecimal.valueOf(0)));

        Assertions.assertEquals(0,
                currencyConverterService.reverseConversion(null).compareTo(BigDecimal.valueOf(0)));

        Assertions.assertEquals(0,
                currencyConverterService.reverseConversion(BigDecimal.valueOf(0.8)).compareTo(BigDecimal.valueOf(1.25)));

        Assertions.assertEquals(0,
                currencyConverterService.reverseConversion(BigDecimal.valueOf(1.25)).compareTo(BigDecimal.valueOf(.8)));

        Assertions.assertEquals(0,
                currencyConverterService.reverseConversion(BigDecimal.valueOf(0.65)).compareTo(BigDecimal.valueOf(1.53846)));
        Assertions.assertEquals(0,
                currencyConverterService.reverseConversion(BigDecimal.valueOf(1.53846)).compareTo(BigDecimal.valueOf(.65)));
    }

    @Test
    public void connectedConversionRates() {
        List<List<Integer>> connectedCurrencies = currencyConverterService.getConnectedConversionRates(currencyConverterService.getAllConversionRates());
        Assertions.assertEquals(3, connectedCurrencies.size());
        Assertions.assertTrue(connectedCurrencies.get(0).contains(0));
        Assertions.assertTrue(connectedCurrencies.get(1).contains(1));
        Assertions.assertTrue(connectedCurrencies.get(1).contains(2));
        Assertions.assertTrue(connectedCurrencies.get(1).contains(3));
        Assertions.assertTrue(connectedCurrencies.get(1).contains(4));
        Assertions.assertTrue(connectedCurrencies.get(2).contains(5));
        Assertions.assertTrue(connectedCurrencies.get(2).contains(6));
    }

    @Test
    public void testCurrencyConnectionList() {
        List<ConversionRates> allConversionRates = currencyConverterService.getAllConversionRates();


        Assertions.assertEquals(new ArrayList<>(),
                currencyConverterService.currencyConnectionList(0L, 1L, allConversionRates));

        Assertions.assertEquals(new ArrayList<>(),
                currencyConverterService.currencyConnectionList(5L, 8L, allConversionRates));

        Assertions.assertEquals(new ArrayList<>(),
                currencyConverterService.currencyConnectionList(1L, 6L, allConversionRates));

        Assertions.assertEquals(Arrays.asList(1, 2, 3, 4, 7, 8, 9),
                currencyConverterService.currencyConnectionList(1L, 2L, allConversionRates));
        Assertions.assertEquals(Arrays.asList(1, 2, 3, 4, 7, 8, 9),
                currencyConverterService.currencyConnectionList(2L, 4L, allConversionRates));

        Assertions.assertEquals(Arrays.asList(5, 6),
                currencyConverterService.currencyConnectionList(5L, 6L, allConversionRates));


    }

    @Test
    public void testShortestPathBetweenConversionRates() {


        List<ConversionRates> allConversionRates = currencyConverterService.getAllConversionRates();
        Assertions.assertEquals(Arrays.asList(1L, 2L, 3L), currencyConverterService.shortestPathBetweenConversionRates(1L, 3L, allConversionRates));
        Assertions.assertEquals(Arrays.asList(4L, 1L, 2L, 7L), currencyConverterService.shortestPathBetweenConversionRates(4L, 7L, allConversionRates));
        Assertions.assertEquals(Arrays.asList(9L, 7L, 2L, 1L), currencyConverterService.shortestPathBetweenConversionRates(9L, 1L, allConversionRates));
        Assertions.assertEquals(Arrays.asList(1L, 2L, 7L, 9L), currencyConverterService.shortestPathBetweenConversionRates(1L, 9L, allConversionRates));
        Assertions.assertEquals(Arrays.asList(1L, 4L), currencyConverterService.shortestPathBetweenConversionRates(1L, 4L, allConversionRates));
        Assertions.assertNull(currencyConverterService.shortestPathBetweenConversionRates(2L, 6L, allConversionRates));
        Assertions.assertNull(currencyConverterService.shortestPathBetweenConversionRates(0L, 3L, allConversionRates));
    }

}
