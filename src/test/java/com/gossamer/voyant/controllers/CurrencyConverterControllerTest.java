package com.gossamer.voyant.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gossamer.voyant.entities.ConversionRates;
import com.gossamer.voyant.model.ConversionRateWithCountryNames;
import com.gossamer.voyant.model.CurrencyData;
import com.gossamer.voyant.services.CurrencyConverterService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CurrencyConverterControllerTest {
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
        Mockito.when(currencyConverterService.getAllConversionRatesWithCountryNames()).thenReturn(
                Arrays.asList(ConversionRateWithCountryNames.builder().originCountry("USA")
                                .conversionCountry("USD").conversionRate(BigDecimal.valueOf(.8)).build(),
                        ConversionRateWithCountryNames.builder().originCountry("USA")
                                .conversionCountry("YEN").conversionRate(BigDecimal.valueOf(1.2)).build()));
        MvcResult result = this.mockMvc.perform(get("/currencyConvertor/getAllConversionRatesWithCountryName"))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        Assertions.assertEquals("[{\"originCountry\":\"USA\",\"conversionCountry\":\"USD\",\"conversionRate\":0.8}," +
                        "{\"originCountry\":\"USA\",\"conversionCountry\":\"YEN\",\"conversionRate\":1.2}]",
                result.getResponse().getContentAsString());

        Mockito.verify(currencyConverterService, Mockito.times(1))
                .getAllConversionRatesWithCountryNames();

    }

    @Test
    void getCountryIdShouldCallGetCountryIdAndPassInArgument() throws Exception {
        Mockito.when(currencyConverterService.getConversionRate("USA", "USD"))
                .thenReturn(BigDecimal.valueOf(.9));
        MvcResult result = this.mockMvc.perform(get("/currencyConvertor/getConversionRate")
                        .param("originCountry", "USA").param("conversionCountry", "USD"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Assertions.assertEquals("0.9", result.getResponse().getContentAsString());

        Assertions.assertEquals("USA", result.getRequest().getParameter("originCountry"));
        Assertions.assertEquals("USD", result.getRequest().getParameter("conversionCountry"));

        Mockito.verify(currencyConverterService, Mockito.times(1)).getConversionRate("USA", "USD");
    }

    @Test
    void postAddCurrencyConversionsShouldCallAddCurrencyConversions() throws Exception {
        HashMap<String, List<List<String>>> payLoad = new HashMap<>();
        payLoad.put("currencyData", List.of(
                Arrays.asList("YEN", "USD", ".01")
        ));
        String requestBody = new ObjectMapper().writeValueAsString(payLoad);

        CurrencyData payLoadExpectation = CurrencyData.builder().currencyData(List.of(
                Arrays.asList("YEN", "USD", ".01")
        )).build();

        Mockito.doNothing().when(currencyConverterService).updateCurrencyData(payLoadExpectation);
        MvcResult result = this.mockMvc.perform(RestDocumentationRequestBuilders
                        .post("/currencyConvertor/updateCurrencyConversions").contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print()).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Mockito.verify(currencyConverterService, Mockito.times(1)).updateCurrencyData(Mockito.any());
    }
}
