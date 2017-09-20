package model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Andrew1 on 5/31/17.
 */

public class LocationsTest {
    @Test
    public void testGetsAndSets(){
        Location location = new Location("1", "1", "1", "1");
        Location location2 = new Location("2", "2", "2", "2");
        Location[] data = {location, location};
        Location[] data2 = {location2, location2};
        Locations locations = new Locations(data);
        locations.setData(data2);
        assertEquals(data2[0], locations.getData()[0]);
        assertEquals(data2[1], locations.getData()[1]);
    }
}
