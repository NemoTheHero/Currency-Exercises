package com.gossamer.voyant.services;

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
public class CountriesServiceTest {
    @Autowired
    CountriesService countriesService;


    @Test
    public void shouldHaveCountriesOnStartup() {
        List<Countries> countriesList = countriesService.getAllCountries();
        Assertions.assertEquals(6, countriesList.size());
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
}
