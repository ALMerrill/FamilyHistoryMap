package model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Andrew1 on 5/31/17.
 */

public class LocationTest {
    @Test
    public void testGetsAndSets(){
        Location location = new Location("", "", "", "");
        location.setCity("city");
        assertEquals("city", location.getCity());
        location.setCountry("country");
        assertEquals("country", location.getCountry());
        location.setLatitude("lat");
        assertEquals("lat", location.getLatitude());
        location.setLongitude("long");
        assertEquals("long", location.getLongitude());
    }
}
