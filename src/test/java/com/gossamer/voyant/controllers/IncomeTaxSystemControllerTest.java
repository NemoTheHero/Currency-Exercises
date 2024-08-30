package com.gossamer.voyant.controllers;

import com.gossamer.voyant.entities.ConversionRates;
import com.gossamer.voyant.entities.IncomeTaxBrackets;
import com.gossamer.voyant.services.IncomeTaxSystemService;
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
public class IncomeTaxSystemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    IncomeTaxSystemService incomeTaxSystemService;

    @Test
    void getAllIncomeTaxesBracketsShouldCallGetAllIncomeTaxesBrackets() throws Exception {
        Mockito.when(incomeTaxSystemService.getAllTaxBrackets()).thenReturn(
                Arrays.asList(
                        IncomeTaxBrackets.builder().id(1L)
                                .lowerLimit(BigDecimal.valueOf(0))
                                .higherLimit(BigDecimal.valueOf(10000))
                                .taxRate(BigDecimal.valueOf(.1)).build(),
                        IncomeTaxBrackets.builder().id(2L)
                                .lowerLimit(BigDecimal.valueOf(10000))
                                .higherLimit(BigDecimal.valueOf(20000))
                                .taxRate(BigDecimal.valueOf(.12)).build()
                        ));
        MvcResult result = this.mockMvc.perform(get("/incomeTax/getAllIncomeTaxesBrackets"))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        Assertions.assertEquals("[{\"id\":1,\"lowerLimit\":0,\"higherLimit\":10000,\"taxRate\":0.1}," +
                        "{\"id\":2,\"lowerLimit\":10000,\"higherLimit\":20000,\"taxRate\":0.12}]",
                result.getResponse().getContentAsString());

        Mockito.verify(incomeTaxSystemService, Mockito.times(1)).getAllTaxBrackets();
    }

    @Test
    void getIncomeTaxBracketsForIncomeShouldCallGetIncomeBracketsForIncomeLowToHigh() throws Exception {
        Mockito.when(incomeTaxSystemService.getIncomeBracketsForIncomeLowToHigh(BigDecimal.valueOf(30000))).thenReturn(
                Arrays.asList(
                        IncomeTaxBrackets.builder().id(1L)
                                .lowerLimit(BigDecimal.valueOf(0))
                                .higherLimit(BigDecimal.valueOf(10000))
                                .taxRate(BigDecimal.valueOf(.1)).build(),
                        IncomeTaxBrackets.builder().id(2L)
                                .lowerLimit(BigDecimal.valueOf(10000))
                                .higherLimit(BigDecimal.valueOf(20000))
                                .taxRate(BigDecimal.valueOf(.12)).build()
                ));
        MvcResult result = this.mockMvc.perform(get("/incomeTax/getIncomeTaxBracketsForIncome")
                        .param("income", "30000"))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        Assertions.assertEquals("[{\"id\":1,\"lowerLimit\":0,\"higherLimit\":10000,\"taxRate\":0.1}," +
                        "{\"id\":2,\"lowerLimit\":10000,\"higherLimit\":20000,\"taxRate\":0.12}]",
                result.getResponse().getContentAsString());

        Mockito.verify(incomeTaxSystemService, Mockito.times(1))
                .getIncomeBracketsForIncomeLowToHigh(BigDecimal.valueOf(30000));
    }

    @Test
    void getCalculateIncomeTaxShouldCallCalculateIncomeTax() throws Exception {
        Mockito.when(incomeTaxSystemService.calculateIncomeTax(BigDecimal.valueOf(30000))).thenReturn(
                BigDecimal.valueOf(3000));
        MvcResult result = this.mockMvc.perform(get("/incomeTax/calculateIncomeTax")
                        .param("income", "30000"))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        Assertions.assertEquals("3000",
                result.getResponse().getContentAsString());

        Mockito.verify(incomeTaxSystemService, Mockito.times(1))
                .calculateIncomeTax(BigDecimal.valueOf(30000));
    }

    @Test
    void getDetermineMarginalTaxRateShouldCallDetermineMarginalTaxRate() throws Exception {
        Mockito.when(incomeTaxSystemService.determineMarginalTaxRate(BigDecimal.valueOf(30000))).thenReturn(
                BigDecimal.valueOf(.12));
        MvcResult result = this.mockMvc.perform(get("/incomeTax/determineMarginalTaxRate")
                        .param("income", "30000"))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        Assertions.assertEquals("0.12",
                result.getResponse().getContentAsString());

        Mockito.verify(incomeTaxSystemService, Mockito.times(1))
                .determineMarginalTaxRate(BigDecimal.valueOf(30000));
    }

    @Test
    void getDetermineEffectiveTaxRateShouldCallDetermineEffectiveTaxRate() throws Exception {
        Mockito.when(incomeTaxSystemService.determineEffectiveTaxRate(BigDecimal.valueOf(30000))).thenReturn(
                BigDecimal.valueOf(.12));
        MvcResult result = this.mockMvc.perform(get("/incomeTax/determineEffectiveTaxRate")
                        .param("income", "30000"))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        Assertions.assertEquals("0.12",
                result.getResponse().getContentAsString());

        Mockito.verify(incomeTaxSystemService, Mockito.times(1))
                .determineEffectiveTaxRate(BigDecimal.valueOf(30000));
    }
}
