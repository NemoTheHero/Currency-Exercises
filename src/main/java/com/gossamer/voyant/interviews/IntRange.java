/**
 * Copyright 2006-2018 Voyant, Inc..  All Rights Reserved.
 * Date: 12/1/2017
 * Time: 10:01 AM
 */

package com.gossamer.voyant.interviews;

import org.springframework.stereotype.Component;

/**
 * Implementation of a classic Integer range,
 *
 * Assume  values are inclusive, and highest value is exclusive
 * e.g. [2,5] [6,10]
 *
 *
 * Implement this class, and the overlaps method below
 *
 *
 *
 */
@Component
public class IntRange {


    /**
     * Determines whether 2 ranges overlap: e.g.
     * [2,5] and [4,6] => true
     * [2,4] and [5,6] => false
     * [2,5] and [5,6] => false
     *
     * note: The signature of this method's parameters are incomplete.  Please specify as part of the exercise
     *
     * @return true if the provided range overlaps with this range
     */
    public boolean overlaps(int[] array1, int[] array2) {

        if (array1 == null || array1.length != 2 ||array2 == null || array2.length != 2) {
            return false;
        }

        int smallestInArray1 = Math.min(array1[0], array1[1]);
        int largestInArray1 = Math.max(array1[0], array1[1]);

        int smallestInArray2 = Math.min(array2[0], array2[1]);
        int largestInArray2 = Math.max(array2[0], array2[1]);

        return largestInArray1 >= smallestInArray2 && largestInArray2 >= smallestInArray1;
    }

}
