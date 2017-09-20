package model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Andrew1 on 5/31/17.
 */

public class LastNamesTest {
    @Test
    public void testGetsAndSets(){
        String[] data = {"1", "2"};
        String[] data2 = {"3", "4"};
        LastNames names = new LastNames(data);
        names.setData(data2);
        assertEquals(data2[0], names.getData()[0]);
        assertEquals(data2[1], names.getData()[1]);
    }
}
