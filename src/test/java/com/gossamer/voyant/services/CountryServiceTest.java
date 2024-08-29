package com.gossamer.voyant.services;

import com.gossamer.voyant.entities.Country;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CountryServiceTest {
    @Autowired
    CountriesService countriesService;



    @Test
    public void shouldHaveCountriesOnStartup() {
        List<Country> countryList = countriesService.getAllCountries();
        Assertions.assertEquals(6, countryList.size());
    }

    @Test
    public void countriesShouldAutoIncrementOnStartup() {
        Long countryId = countriesService.getCountryId("GDP");
        Assertions.assertEquals(2, countryId);
    }

    @Test(expected = ResponseStatusException.class)
    public void getCountryIdShouldReturn404whenCountryNotFound() {
        countriesService.getCountryId("USA");
    }

    @Test
    public void shouldAddCountry() {
        countriesService.addCountry("USA");
        Assertions.assertEquals(7, countriesService.getCountryId("USA"));
    }

    @Test
    public void shouldMapCountriesToMap() {
        Map<Long,String> countriesToMap = countriesService.countriesToMap();

        Assertions.assertEquals("USD", countriesToMap.get(1L));
        Assertions.assertEquals("GDP", countriesToMap.get(2L));
        Assertions.assertEquals("EUR", countriesToMap.get(3L));
        Assertions.assertEquals("CAD", countriesToMap.get(4L));
        Assertions.assertEquals("YEN", countriesToMap.get(5L));
        Assertions.assertEquals("AUS", countriesToMap.get(6L));
    }
}
