package com.gossamer.voyant.controllers;

import com.gossamer.voyant.entities.ConversionRates;
import com.gossamer.voyant.entities.Country;
import com.gossamer.voyant.model.ConversionRatesWithCountryName;
import com.gossamer.voyant.services.CountriesService;
import com.gossamer.voyant.services.CurrencyConverterService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CurrencyConverterTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CurrencyConverterService currencyConverterService;

    @Test
    void getAllConversionRatesShouldCallGetAllConversionRates() throws Exception {
        Mockito.when(currencyConverterService.getAllConversionRates()).thenReturn(
                Arrays.asList(ConversionRates.builder().id(1L).originCountryFid(1L)
                                .conversionCountryFid(2L).conversionRate(BigDecimal.valueOf(.8)).build(),
                        ConversionRates.builder().id(2L).originCountryFid(2L)
                                .conversionCountryFid(3L).conversionRate(BigDecimal.valueOf(.6)).build()));
        MvcResult result = this.mockMvc.perform(get("/currencyConvertor/getAllConversionRates"))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        Assertions.assertEquals("[{\"id\":1,\"originCountryFid\":1,\"conversionCountryFid\":2,\"conversionRate\":0.8}," +
                        "{\"id\":2,\"originCountryFid\":2,\"conversionCountryFid\":3,\"conversionRate\":0.6}]",
                result.getResponse().getContentAsString());

        Mockito.verify(currencyConverterService, Mockito.times(1)).getAllConversionRates();
    }

    @Test
    void getAllConversionRatesWithNameShouldCallGetAllConversionRatesWithName() throws Exception {
        Mockito.when(currencyConverterService.getAllConversionRatesWithCountryName()).thenReturn(
                Arrays.asList(ConversionRatesWithCountryName.builder().originCountry("USA")
                                .conversionCountry("USD").conversionRate(BigDecimal.valueOf(.8)).build(),
                        ConversionRatesWithCountryName.builder().originCountry("USA")
                                .conversionCountry("YEN").conversionRate(BigDecimal.valueOf(1.2)).build()));
        MvcResult result = this.mockMvc.perform(get("/currencyConvertor/getAllConversionRatesWithCountryName"))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        Assertions.assertEquals("[{\"originCountry\":\"USA\",\"conversionCountry\":\"USD\",\"conversionRate\":0.8}," +
                        "{\"originCountry\":\"USA\",\"conversionCountry\":\"YEN\",\"conversionRate\":1.2}]",
                result.getResponse().getContentAsString());

        Mockito.verify(currencyConverterService, Mockito.times(1)).getAllConversionRatesWithCountryName();

    }
}
