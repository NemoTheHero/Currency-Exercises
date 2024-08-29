/**
 * Copyright 2006-2018 Voyant, Inc..  All Rights Reserved.
 * <p>
 * Date: 12/1/2017
 * Time: 12:10 PM
 */

package com.gossamer.voyant.interviews;

import junit.framework.TestCase;


/**
 */
public class CurrencyConverterTest extends TestCase {

    public CurrencyConverterTest(String s) {
        super(s);
    }


    protected void setUp() throws Exception {

    }

    protected void tearDown() throws Exception {

    }

    public void testOfficial() {
/*
 * From     To      Rate
 * =====================
 * USD      GBP     .72
 * EUR      GBP     .87
 * CAD      USD     .78
 * YEN      AUS     .012
 */
        String[][] data = new String[][] {
                {"USD", "GBP", ".72"},
                {"EUR", "GBP", ".87"},
                {"CAD", "USD", ".78"},
                {"YEN", "AUS", ".012"},
        };



    }

}