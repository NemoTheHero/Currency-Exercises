/**
 * Copyright 2006-2018 Voyant, Inc..  All Rights Reserved.
 * <p>
 * Date: 12/1/2017
 * Time: 10:06 AM
 */

package com.gossamer.voyant.interviews;

/**
 *
 * Progressive Tax regimes often have a table like the following to define how basic income tax is calculated
 * <pre>
 * Bracket  Bracket Range   Tax Rate
 * ========================================
 * 1        0-20,000        10%
 * 2        20,000-50,000   15%
 * 3        50,000-100,000  20%
 * 4        100,000 >       25%
 * </pre>
 * <p>
 * For this class, create a data structure to represent this tax regime, and then use your data structure to implement
 * the  method
 * <strong>calculateIncomeTax(double income)</strong>
 * </p>
 *
 * <p>
 *     For this exercise you can hard code the above data to initialize your IncomeTaxSystem, but your
 *     data structure should be flexible enough to represent other tables with differing ranges and less or more brackets.
 * </p>
 *
 * <p>
 * Addiontally implement:
 *
 * methods
 * <strong>determineMarginalTaxRate(income)</strong>
 * and
 * <strong>determineEffectiveTaxRate(income)</strong>
 *  methods.
 * </p>
 */
public class IncomeTaxSystem {




    /**
     * Calculate the total tax on the income given the tax regime modelled by this IncomeTaxSystem
     * @param income
     * @return
     */
    public double calculateIncomeTax(double income) {
        return 0;
    }

    /**
     * Given an income, determine the marginal tax rate, meaning the tax rate applied to the last dollar of income.
     * @param income
     * @return
     */
    public double determineMarginalTaxRate(double income) {
        return 0;
    }

    /**
     * Given an income, determine the average tax rate that the entire income was taxed at
     * @param income
     * @return
     */
    public double determineEffectiveTaxRate(double income) {
        return 0;
    }



    public static void main(String[] args) {

    }
}
