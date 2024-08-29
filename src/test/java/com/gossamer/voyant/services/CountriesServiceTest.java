package com.gossamer.voyant.services;

import com.gossamer.voyant.entities.Countries;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CountriesServiceTest {
    @Autowired
    CountriesService countriesService;


    @Test
    public void dbShouldHaveCountries() {
        List<Countries> countriesList = countriesService.getAllCountries();
        Assertions.assertEquals(5, countriesList.size());
    }
}
