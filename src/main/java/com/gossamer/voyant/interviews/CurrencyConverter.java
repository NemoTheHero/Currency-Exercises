/**
 * Copyright 2006-2018 Voyant, Inc..  All Rights Reserved.
 * <p>
 * User: skip@planwithvoyant.com
 * Date: 4/5/2018
 * Time: 10:18 AM
 */

package com.gossamer.voyant.interviews;

/**
 * In order to convert the value of a specific currency, say $10, to its equivalent value in British pounds,
 * we need to use a currency conversion table to convert the $10 => £7.20.
 *
 * In this class, we implement a currency conversion table data structure given input like the following:
 *
 * Given a Currency Code conversion table like
 *
 * <pre>
 * From     To      Rate
 * =====================
 * USD      GBP     .72
 * EUR      GBP     .87
 * CAD      USD     .78
 * YEN      AUS     .012
 *
 * </pre>
 * <p>
 * Implement a data structure to store the currency data, and implement the
 * getConversionRate(from, to) method.
 * </p>
 *
 * <p>
 * <strong>Assume symmetry </strong> between source and target currency. e.g. if USD to GBP = .72,
 * then GBP to USD is equivalent but in the opposite direction.
 * In real currency exchange markets, this can be different at times.
 *
 * </p>
 *
 * <p><strong>Hints:</strong>
 * <p>
 * Perhaps, initially make your implementation work for just the direct conversions specified and their reverse conversions.
 * </p>
 * Then, make your implementation work for all possible conversions given the data.
 * </p>
 *
 * <p>
 * It may help you to have some method to output the current state of your data structure
 * </p>
 *
 * <p>
 * <strong>Extra credit:</strong>
 * <p>
 * <b>Note: Even if you do not implement the following, be prepared to talk about how you could implement them with your solution
 * and the implications of needing to provide thread-safety for querying the data or updating it.
 * </b>
 * </p>
 * <ol>
 * <li> Implement your class in such a manner that it can be used in a multi-threaded environment.</li>
 * <li> provide a method to load updated currency data in a thread-safe manner.</li>
 * </ol>
 * </p>
 * <p>
 *     Assume currency updates happen a few times a day.  At most once an hour.
 *  </p>
 *
 * @author <a href="mailto:skip@planwithvoyant.com">Skip Walker</a>
 */
public class CurrencyConverter {

    // -------------------------------------------------------------- Constants


    // ------------------------------------------------------- Static Variables



    // ------------------------------------------------------ Static Init Block


    // ----------------------------------------------------- Instance Variables


    // ----------------------------------------------------------- Constructors

    /**
     * Currency table like the above is passed in as a simple 2D string array
     * where each row is [from, to, rate]
     * e.g. currencyData[i][0] = from
     * e.g. currencyData[i][1] = to
     * e.g. currencyData[i][2] = rate
     * @param currencyData
     */
    public CurrencyConverter(String[][] currencyData) {
    }

    // --------------------------------------------------------- Public Methods

    /**
     * Get the current conversion rate going from currency <b>from</b> to currency <b>to</b>
     * @param from the source currency   (e.g. USD)
     * @param to the target currency     (e.g. GBP)
     * @return null if conversion cannot be performed with current data.
     */
    public Double getConversionRate(String from, String to) {
        return null;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CurrencyConverter{");
        // TODO: implement this to help your debugging, if you need to
        sb.append('}');
        return sb.toString();
    }

    // ---------------------------------------------------------- Other Methods

    // ---------------------------------------------------------- Inner Classes

    // ------------------------------------------------------------ Main Method

    public static void main(String[] args) {

    }

}
