package com.gossamer.voyant.services;

import com.gossamer.voyant.dao.ConversionRatesDao;
import com.gossamer.voyant.entities.ConversionRates;
import com.gossamer.voyant.model.ConversionRateWithCountryNames;
import com.gossamer.voyant.model.CurrencyData;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class CurrencyConverterService {

    private final ConversionRatesDao conversionRatesDao;

    private final CountriesService countriesService;


    public CurrencyConverterService(ConversionRatesDao conversionRatesDao, CountriesService countriesService) {
        this.conversionRatesDao = conversionRatesDao;
        this.countriesService = countriesService;
    }

    public List<ConversionRateWithCountryNames> getAllConversionRatesWithCountryNames() {
        Map<Long, String> countriesToMap = countriesService.countriesToMap();
        List<ConversionRates> allConversionRates = getAllConversionRates();
        List<ConversionRateWithCountryNames> conversionRateWithCountryNames = new ArrayList<>();

        allConversionRates.forEach(conversionRates -> conversionRateWithCountryNames.add(
                ConversionRateWithCountryNames.builder()
                        .originCountry(countriesToMap.get(conversionRates.getOriginCountryFid()))
                        .conversionCountry(countriesToMap.get(conversionRates.getConversionCountryFid()))
                        .conversionRate(conversionRates.getConversionRate())
                        .build()));
        return conversionRateWithCountryNames;
    }

    public List<ConversionRates> getAllConversionRates() {
        return (List<ConversionRates>) conversionRatesDao.findAll();
    }

    public List<ConversionRates> getConversionRatesForCountry(Long originCountryId) {
        return conversionRatesDao.findByOriginCountryFid(originCountryId);
    }

    public List<ConversionRates> getRateByOriginAndConversionCurrency(Long originCurrencyId, Long conversionCurrencyId) {
        return conversionRatesDao.findByOriginCountryFidAndConversionCountryFid(originCurrencyId, conversionCurrencyId);
    }

    public BigDecimal getConversionRate(String originCountry, String conversionCountry) {

        Long originCountryFid = countriesService.getCountryId(originCountry);
        Long conversionCountryFid = countriesService.getCountryId(conversionCountry);
        if(Objects.equals(originCountryFid, conversionCountryFid)) {
            return BigDecimal.ONE;
        }
        List<ConversionRates> conversionRates = getRateByOriginAndConversionCurrency(originCountryFid, conversionCountryFid);
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

                if (Double.parseDouble(item.get(2)) <= 0) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            String.format("Conversion Rate cannot be 0 or Negative - %s", item.get(2)));
                }
            } catch (ResponseStatusException e) {
                throw e;
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
        List<Long> pathFromOriginToTarget = shortestPathBetweenConversionRates(originCountryFid, conversionCountryFid, allConversionRates);


        if (pathFromOriginToTarget == null) {
            return null;
        }
        for (Long path : pathFromOriginToTarget) {
            System.out.print(path + " ");
        }
        MultiKeyMap<Long, BigDecimal> conversionRateMap = convertConversionRateArrayToMap(allConversionRates);
        MultiKeyMap<Long, BigDecimal> newDeterminedConversionRates = convertConversionRateArrayToMap(allConversionRates);
        BigDecimal finalConversionRate = BigDecimal.ONE;
        int originIndex = 0;

        //filter out rates with only ones that connected
        for (int i = 0; i < pathFromOriginToTarget.size() - 1; i++) {
            //check if adjacent path exists to node if not create one

            int nextNode = i + 1;

            if (conversionRateMap.containsKey(pathFromOriginToTarget.get(i), pathFromOriginToTarget.get(nextNode))) {

                BigDecimal conversionValue = conversionRateMap.get(pathFromOriginToTarget.get(i), pathFromOriginToTarget.get(nextNode));
                finalConversionRate = finalConversionRate.multiply(conversionValue);
                //todo make duplication into a function
                //check if conversion exists from origin to this node if it doesnt determine it to be added
                if (!conversionRateMap.containsKey(pathFromOriginToTarget.get(originIndex), pathFromOriginToTarget.get(nextNode))) {
                    newDeterminedConversionRates.put(pathFromOriginToTarget.get(originIndex),
                            pathFromOriginToTarget.get(nextNode), finalConversionRate);
                }
                //check if conversion exists from current node to origin if it doesnt determine it to be added
                if (!conversionRateMap.containsKey(pathFromOriginToTarget.get(nextNode), pathFromOriginToTarget.get(originIndex))) {
                    newDeterminedConversionRates.put(pathFromOriginToTarget.get(nextNode),
                            pathFromOriginToTarget.get(originIndex), reverseConversion(finalConversionRate));
                }

                //check if reverse exists, if not add it to newDeterminedConversionRates

                if (!conversionRateMap.containsKey(pathFromOriginToTarget.get(nextNode), pathFromOriginToTarget.get(i))) {
                    newDeterminedConversionRates.put(pathFromOriginToTarget.get(nextNode),
                            pathFromOriginToTarget.get(i), reverseConversion(conversionValue));
                }

            } else {


                BigDecimal conversionValue = conversionRateMap.get(pathFromOriginToTarget.get(nextNode), pathFromOriginToTarget.get(i));
                finalConversionRate = finalConversionRate.multiply(reverseConversion(conversionValue));

                //check if conversion exists from origin to this node if it doesnt determine it to be added
                if (!conversionRateMap.containsKey(pathFromOriginToTarget.get(originIndex), pathFromOriginToTarget.get(nextNode))) {
                    newDeterminedConversionRates.put(pathFromOriginToTarget.get(originIndex),
                            pathFromOriginToTarget.get(nextNode), finalConversionRate);
                }
                //check if conversion exists from current node to origin if it doesnt determine it to be added

                if (!conversionRateMap.containsKey(pathFromOriginToTarget.get(nextNode), pathFromOriginToTarget.get(originIndex))) {
                    newDeterminedConversionRates.put(pathFromOriginToTarget.get(nextNode),
                            pathFromOriginToTarget.get(originIndex), reverseConversion(finalConversionRate));
                }

                // add to newDeterminedConversionRates
                if (!conversionRateMap.containsKey(pathFromOriginToTarget.get(i), pathFromOriginToTarget.get(nextNode))) {
                    newDeterminedConversionRates.put(pathFromOriginToTarget.get(i),
                            pathFromOriginToTarget.get(nextNode), reverseConversion(conversionValue));
                }

            }

        }
        addNewlyDeterminedRatesToDB(newDeterminedConversionRates);
        return finalConversionRate;
    }


    MultiKeyMap<Long, BigDecimal> convertConversionRateArrayToMap(List<ConversionRates> conversionRatesList) {
        MultiKeyMap<Long, BigDecimal> multiKeyMap = new MultiKeyMap<>();
        for (ConversionRates conversionRate : conversionRatesList) {
            multiKeyMap.put(conversionRate.getOriginCountryFid(), conversionRate.getConversionCountryFid(),
                    conversionRate.getConversionRate());
        }
        return multiKeyMap;

    }

    public void addNewlyDeterminedRatesToDB(MultiKeyMap<Long, BigDecimal> newlyDeterminedRatesMap) {
        CurrencyData currencyData = new CurrencyData();
        currencyData.setCurrencyData(new ArrayList<>());
        for (Map.Entry<MultiKey<? extends Long>, BigDecimal> entry : newlyDeterminedRatesMap.entrySet()) {
            currencyData.getCurrencyData()
                    .add(List.of(
                            entry.getKey().getKey(0).toString(),
                            entry.getKey().getKey(1).toString(),
                            entry.getValue().toString()));
        }
        //todo make a function for this
        currencyData.getCurrencyData().forEach(item -> {
            Long originCountryId = Long.valueOf(item.get(0));
            Long conversionCountryId = Long.valueOf(item.get(1));
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


    static void bfs(List<List<Integer>> graph, int S,
                    List<Integer> par, List<Integer> dist) {
        // Queue to store the nodes in the order they are
        // visited
        Queue<Integer> q = new LinkedList<>();
        // Mark the distance of the source node as 0
        dist.set(S, 0);
        // Push the source node to the queue
        q.add(S);

        // Iterate until the queue is not empty
        while (!q.isEmpty()) {
            // Pop the node at the front of the queue
            int node = q.poll();

            // Explore all the neighbors of the current node
            for (int neighbor : graph.get(node)) {
                // Check if the neighboring node is not
                // visited
                if (dist.get(neighbor)
                        == Integer.MAX_VALUE) {
                    // Mark the current node as the parent
                    // of the neighboring node
                    par.set(neighbor, node);
                    // Mark the distance of the neighboring
                    // node as the distance of the current
                    // node + 1
                    dist.set(neighbor, dist.get(node) + 1);
                    // Insert the neighboring node to the
                    // queue
                    q.add(neighbor);
                }
            }
        }
    }

    public List<Long> shortestPathBetweenConversionRates(Long originCurrencyId,
                                                         Long conversionCurrencyId,
                                                         List<ConversionRates> allConversionRates) {


        List<List<Integer>> edges = new ArrayList<>();
        List<Long> uniqueCurrenciesList = new ArrayList<>();


        allConversionRates.forEach(conversionRates -> {
            if (!uniqueCurrenciesList.contains(conversionRates.getOriginCountryFid())) {
                uniqueCurrenciesList.add(conversionRates.getOriginCountryFid());
            }
            if (!uniqueCurrenciesList.contains(conversionRates.getConversionCountryFid())) {
                uniqueCurrenciesList.add(conversionRates.getConversionCountryFid());
            }
        });

        // Number of vertices in the graph
        int V = uniqueCurrenciesList.size() + 1;


        // Add edges to the graph
        allConversionRates.forEach(conversionRates -> edges.add(Arrays.asList(conversionRates.getOriginCountryFid().intValue(),
                conversionRates.getConversionCountryFid().intValue())));


        List<List<Integer>> graph = new ArrayList<>(V);
        for (int i = 0; i < V; i++) {
            graph.add(new ArrayList<>());
        }

        for (List<Integer> edge : edges) {
            graph.get(edge.get(0)).add(edge.get(1));
            graph.get(edge.get(1)).add(edge.get(0));
        }

        return getShortestDistance(graph, originCurrencyId.intValue(), conversionCurrencyId.intValue(), V);

    }

    private List<Long> getShortestDistance(List<List<Integer>> graph, int S,
                                           int D, int V) {
        // par[] array stores the parent of nodes
        List<Integer> par
                = new ArrayList<>(Collections.nCopies(V, -1));

        // dist[] array stores the distance of nodes from S
        List<Integer> dist = new ArrayList<>(
                Collections.nCopies(V, Integer.MAX_VALUE));

        // Function call to find the distance of all nodes
        // and their parent nodes
        bfs(graph, S, par, dist);

        if (dist.get(D) == Integer.MAX_VALUE) {
            System.out.println(
                    "Source and Destination are not connected");
            return null;
        }

        // List path stores the shortest path
        List<Long> path = new ArrayList<>();
        int currentNode = D;
        path.add((long) D);
        while (par.get(currentNode) != -1) {
            path.add(Long.valueOf(par.get(currentNode)));
            currentNode = par.get(currentNode);
        }

        // Printing path from source to destination
        for (int i = path.size() - 1; i >= 0; i--) {
            System.out.print(path.get(i) + " ");
        }
        System.out.println(" ");

        return path.reversed();
    }


}
