package com.gossamer.voyant.services;

import com.gossamer.voyant.dao.ConversionRatesDao;
import com.gossamer.voyant.entities.ConversionRates;
import com.gossamer.voyant.model.ConversionRatesWithCountryName;
import com.gossamer.voyant.model.CurrencyData;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CurrencyConverterService {

    private final ConversionRatesDao conversionRatesDao;

    private final CountriesService countriesService;


    public CurrencyConverterService(ConversionRatesDao conversionRatesDao, CountriesService countriesService) {
        this.conversionRatesDao = conversionRatesDao;
        this.countriesService = countriesService;
    }

    public List<ConversionRatesWithCountryName> getAllConversionRatesWithCountryName() {
        Map<Long, String> countriesToMap = countriesService.countriesToMap();
        List<ConversionRates> allConversionRates = getAllConversionRates();
        List<ConversionRatesWithCountryName> conversionRatesWithCountryNames = new ArrayList<>();

        allConversionRates.forEach(conversionRates -> conversionRatesWithCountryNames.add(
                ConversionRatesWithCountryName.builder()
                        .originCountry(countriesToMap.get(conversionRates.getOriginCountryFid()))
                        .conversionCountry(countriesToMap.get(conversionRates.getConversionCountryFid()))
                        .conversionRate(conversionRates.getConversionRate())
                        .build()));
        return conversionRatesWithCountryNames;
    }

    public List<ConversionRates> getAllConversionRates() {
        return (List<ConversionRates>) conversionRatesDao.findAll();
    }

    public BigDecimal getConversionRate(String originCountry, String conversionCountry) {
        Long originCountryFid = countriesService.getCountryId(originCountry);
        Long conversionCountryFid = countriesService.getCountryId(conversionCountry);
        List<ConversionRates> conversionRates = conversionRatesDao.findByOriginCountryFidAndConversionCountryFid(originCountryFid, conversionCountryFid);
        if (conversionRates.isEmpty()) {
            //check for inverse
            List<ConversionRates> inverseConversion = conversionRatesDao.findByOriginCountryFidAndConversionCountryFid(conversionCountryFid, originCountryFid);
            if (inverseConversion.isEmpty()) {
                return null;
            }
            return reverseConversion(inverseConversion.get(0).getConversionRate());
        }
        return conversionRates.get(0).getConversionRate();
    }

    public BigDecimal reverseConversion(BigDecimal exchangeRate) {
        if (exchangeRate == null || exchangeRate.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(1.000).divide(exchangeRate, 5, RoundingMode.HALF_EVEN);
    }

    public void updateCurrencyData(CurrencyData currencyData) {
        Map<Long, String> countriesToMap = countriesService.countriesToMap();
        //validate the new currency data
        currencyData.getCurrencyData().forEach(item -> {
            String originCountry = item.get(0);
            if (!countriesToMap.containsValue(originCountry)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Country not in system - %s", originCountry));
            }
            String conversionCountry = item.get(1);
            if (!countriesToMap.containsValue(conversionCountry)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Country not in system - %s", conversionCountry));
            }
            try {
                Double.parseDouble(item.get(2));
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                        String.format("Problem with - %s to %s , %s", item.get(0), item.get(1), item.get(2)));
            }
        });

        currencyData.getCurrencyData().forEach(item -> {
            Long originCountryId = countriesService.getCountryId(item.get(0));
            Long conversionCountryId = countriesService.getCountryId(item.get(1));
            BigDecimal conversionRate = BigDecimal.valueOf(Double.parseDouble(item.get(2)));

            //if row already exists update it instead of adding a new one
            List<ConversionRates> existingConversionRate =
                    conversionRatesDao.findByOriginCountryFidAndConversionCountryFid(originCountryId, conversionCountryId);

            if (existingConversionRate.isEmpty()) {
                conversionRatesDao.save(ConversionRates.builder()
                        .originCountryFid(originCountryId)
                        .conversionCountryFid(conversionCountryId)
                        .conversionRate(conversionRate)
                        .build());
            } else {
                conversionRatesDao.save(ConversionRates.builder()
                        .id(existingConversionRate.get(0).getId())
                        .conversionRate(conversionRate)
                        .originCountryFid(originCountryId)
                        .conversionCountryFid(conversionCountryId)
                        .build());
            }

        });
    }


}
