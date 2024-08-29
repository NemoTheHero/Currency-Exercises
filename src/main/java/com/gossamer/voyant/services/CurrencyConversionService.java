package com.gossamer.voyant.services;

import com.gossamer.voyant.dao.CountriesDao;
import com.gossamer.voyant.dao.ConversionRatesDao;
import com.gossamer.voyant.entities.ConversionRates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
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

    public BigDecimal getConversionRate(String originCountry, String conversionCountry) {
        Long originCountryFid = countriesService.getCountryId(originCountry);
        Long conversionCountryFid = countriesService.getCountryId(conversionCountry);

        List<ConversionRates> conversionRates = conversionRatesDao.findByOriginCountryFidAndConversionCountryFid(originCountryFid, conversionCountryFid);
        if (conversionRates.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("No conversion rate found from %s to %s", originCountry, conversionCountry));
        }
        return conversionRates.get(0).getConversionRate();
    }


}
