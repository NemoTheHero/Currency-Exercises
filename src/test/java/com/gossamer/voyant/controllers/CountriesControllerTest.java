package com.gossamer.voyant.controllers;

import com.gossamer.voyant.entities.Country;
import com.gossamer.voyant.services.CountriesService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.junit.matchers.JUnitMatchers.containsString;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class CountriesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CountriesService countriesService;

    @Test
    void getAllCountriesShouldCallGetAllCountriesOnce() throws Exception {
        Mockito.when(countriesService.getAllCountries()).thenReturn(Arrays.asList(
                        Country.builder().id(1L).country("USA").build(),
                        Country.builder().id(2L).country("USD").build()
                )
        );
        MvcResult result = this.mockMvc.perform(get("/countries/getAllCountries"))
                .andDo(print())
                .andExpect(status().isOk()).andReturn();

        Assertions.assertEquals("[{\"id\":1,\"country\":\"USA\"}," +
                        "{\"id\":2,\"country\":\"USD\"}]",
                result.getResponse().getContentAsString());

        Mockito.verify(countriesService, Mockito.times(1)).getAllCountries();
    }


    @Test
    void getCountryIdShouldCallGetCountryIdAndPassInArgument() throws Exception {
        Mockito.when(countriesService.getCountryId("USA")).thenReturn(
                1L
        );
        MvcResult result = this.mockMvc.perform(
                        get("/countries/getAllCountries")
                                .param("country", "USA"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        Assertions.assertEquals("1",
                result.getResponse().getContentAsString());



        Mockito.verify(countriesService, Mockito.times(1)).getAllCountries();
    }


}
