/**
 * Copyright 2006-2018 Voyant, Inc..  All Rights Reserved.
 * <p>
 * Date: 12/1/2017
 * Time: 12:11 PM
 */

package com.gossamer.voyant.interviews;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IntRangeTest extends TestCase {


    @Autowired IntRange intRange;

    @Test
    public void testIntRanges() throws Exception {
        //test overlapping on fence
        Assert.assertTrue(intRange.overlaps(new int[] {0,1},new int[] {0,1}));
        //test overlapp
        Assert.assertTrue(intRange.overlaps(new int[] {0,1},new int[] {0,2}));
        Assert.assertTrue(intRange.overlaps(new int[] {2,0},new int[] {1,0}));
        Assert.assertTrue(intRange.overlaps(new int[] {1,2},new int[] {0,3}));
        Assert.assertTrue(intRange.overlaps(new int[] {0,3},new int[] {1,2}));
        //test overlap if range is unorded
        Assert.assertTrue(intRange.overlaps(new int[] {3,2},new int[] {0,3}));
        // test overlap if one range is exactly the same
        Assert.assertTrue(intRange.overlaps(new int[] {3,3},new int[] {0,3}));
        //test overlap if both ranges are exactly the same
        Assert.assertTrue(intRange.overlaps(new int[] {3,3},new int[] {3,3}));


        //false if
        // test nulls and ranges less than [x,x]
        Assert.assertFalse(intRange.overlaps(null, null));
        Assert.assertFalse(intRange.overlaps(null,new int[] {0,1}));
        Assert.assertFalse(intRange.overlaps(new int[] {0,1}, null));
        Assert.assertFalse(intRange.overlaps(new int[] {0},new int[] {0,1}));
        Assert.assertFalse(intRange.overlaps(new int[] {0, 1},new int[] {0}));
        // test if ranges not overlapping
        Assert.assertFalse(intRange.overlaps(new int[] {1,1},new int[] {2,3}));
        Assert.assertFalse(intRange.overlaps(new int[] {3,2},new int[] {1,1}));
        Assert.assertFalse(intRange.overlaps(new int[] {1,2},new int[] {3,4}));

    }

}
