package model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Andrew1 on 5/31/17.
 */

public class EventTest {
    @Test
    public void testGetsAndSets(){
        Event Event = new Event("", "", "", "", "", "", "", "", 0);
        Event.setEventID("Event");
        assertEquals("Event", Event.getEventID());
        Event.setDescendant("descendant");
        assertEquals("descendant", Event.getDescendant());
        Event.setCity("city");
        assertEquals("city", Event.getCity());
        Event.setCountry("country");
        assertEquals("country", Event.getCountry());
        Event.setPersonID("id");
        assertEquals("id", Event.getPersonID());
        Event.setEventType("type");
        assertEquals("type", Event.getEventType());
        Event.setLatitude("lat");
        assertEquals("lat", Event.getLatitude());
        Event.setLongitude("long");
        assertEquals("long", Event.getLongitude());
        Event.setYear(1);
        assertEquals(1, Event.getYear());
    }
}
