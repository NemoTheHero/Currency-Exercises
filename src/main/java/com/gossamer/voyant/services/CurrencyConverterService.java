package com.gossamer.voyant.services;

import com.gossamer.voyant.dao.ConversionRatesDao;
import com.gossamer.voyant.entities.ConversionRates;
import com.gossamer.voyant.model.ConversionRatesWithCountryName;
import com.gossamer.voyant.model.CurrencyData;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static java.util.Map.entry;

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
                return getConversionByRelationship(originCountryFid, conversionCountryFid);
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

    public static int merge(int[] parent, int x) {
        if (parent[x] == x)
            return x;
        return merge(parent, parent[x]);
    }

    public static List<List<Integer>> getConnectedConversionRates(int n, List<List<Integer>> edges) {
        int[] parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }

        for (List<Integer> x : edges) {
            parent[merge(parent, x.get(0))] = merge(parent, x.get(1));
        }

        int ans = 0;
        for (int i = 0; i < n; i++) {
            if (parent[i] == i) ans++;
        }

        for (int i = 0; i < n; i++) {
            parent[i] = merge(parent, parent[i]);
        }

        Map<Integer, List<Integer>> m = new HashMap<>();
        for (int i = 0; i < n; i++) {
            m.computeIfAbsent(parent[i], k -> new ArrayList<>()).add(i);
        }

        List<List<Integer>> connectedCurrenciesList = new ArrayList<>();
        for (Map.Entry<Integer, List<Integer>> it : m.entrySet()) {
            List<Integer> l = it.getValue();
            connectedCurrenciesList.add(l);
            for (int x : l) {
                System.out.print(x + " ");
            }
            System.out.println();
        }
        return connectedCurrenciesList;
    }

    //Get all rates that are in a Set together using the Union-Find Algorithm
    List<List<Integer>> getConnectedConversionRates(List<ConversionRates> allConversionRates) {
        List<Long> uniqueCurrenciesList = new ArrayList<>();
        List<List<Integer>> edges = new ArrayList<>();

        allConversionRates.forEach(conversionRates -> {
            if (!uniqueCurrenciesList.contains(conversionRates.getOriginCountryFid())) {
                uniqueCurrenciesList.add(conversionRates.getOriginCountryFid());
            }
            if (!uniqueCurrenciesList.contains(conversionRates.getConversionCountryFid())) {
                uniqueCurrenciesList.add(conversionRates.getConversionCountryFid());
            }
            edges.add(Arrays.asList(conversionRates.getOriginCountryFid().intValue(),
                    conversionRates.getConversionCountryFid().intValue()));
        });

        int n = uniqueCurrenciesList.size() + 1;
        return getConnectedConversionRates(n, edges);
    }

    List<Integer> currencyConnectionList(Long originCurrency, Long conversionCurrency, List<ConversionRates> allConversionRates) {
        List<List<Integer>> connectedConversionRates = getConnectedConversionRates(allConversionRates);
        for (List<Integer> connectedRates : connectedConversionRates) {
            if (connectedRates.contains(originCurrency.intValue()) && connectedRates.contains(conversionCurrency.intValue())) {
                return connectedRates;
            }
        }
        return new ArrayList<>();
    }


    public BigDecimal getConversionByRelationship(Long originCountryFid, Long conversionCountryFid) {

        List<ConversionRates> allConversionRates = getAllConversionRates();
        List<Integer> conversionsInGroup = currencyConnectionList(originCountryFid, conversionCountryFid, allConversionRates);
        if (conversionsInGroup.isEmpty()) {
            return null;
        }

        //filter out rates with only ones that connected
        List<ConversionRates> connectedConversionRates = allConversionRates.stream().filter(conversionRates ->
                conversionsInGroup.contains(conversionRates.getConversionCountryFid().intValue()) ||
                        conversionsInGroup.contains(conversionRates.getOriginCountryFid().intValue())).toList();


        MultiKeyMap<Long,BigDecimal> connectedConversionRateMap = convertRateArrayToMap(connectedConversionRates);

        System.out.println(connectedConversionRateMap);

        return null;
    }

//    HashMap<Long, HashMap<Long, BigDecimal>>
//    determineMissingConversionRates(HashMap<Long, HashMap<Long, BigDecimal>>
//                                            connectedConversionRateMap) {
//
//        MultiKeyMap<Long,BigDecimal>
//
//        return new HashMap<>();
//    }


    MultiKeyMap<Long,BigDecimal> convertRateArrayToMap(List<ConversionRates> conversionRatesList) {
        MultiKeyMap <Long,BigDecimal> multiKeyMap = new MultiKeyMap<>();
        HashMap<Long, HashMap<Long, BigDecimal>> conversionRateMap = new HashMap<>();
        for (ConversionRates conversionRates : conversionRatesList) {
            multiKeyMap.put(conversionRates
                    .getOriginCountryFid(), conversionRates
                    .getConversionCountryFid(), conversionRates.getConversionRate());
        }
        return multiKeyMap;
    }


}
