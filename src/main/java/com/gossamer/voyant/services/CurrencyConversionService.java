package com.gossamer.voyant.services;

import com.gossamer.voyant.dao.CountriesDao;
import com.gossamer.voyant.dao.ConversionRatesDao;
import com.gossamer.voyant.entities.ConversionRates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CurrencyConversionService {

    private final ConversionRatesDao conversionRatesDao;

    private final CountriesService countriesService;


    public CurrencyConversionService(ConversionRatesDao conversionRatesDao, CountriesService countriesService) {
        this.conversionRatesDao = conversionRatesDao;
        this.countriesService = countriesService;
    }

    public List<ConversionRates> getAllConversionRates() {

        return (List<ConversionRates>) conversionRatesDao.findAll();
    }

}
