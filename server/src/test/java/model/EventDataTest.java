package model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Andrew1 on 5/31/17.
 */

public class EventDataTest {
    @Test
    public void testGetsAndSets(){
        EventData eventData = new EventData("", "", "", "");
        eventData.setBirth("birth");
        assertEquals("birth", eventData.getBirth());
        eventData.setBaptism("baptism");
        assertEquals("baptism", eventData.getBaptism());
        eventData.setMarriage("m");
        assertEquals("m", eventData.getMarriage());
        eventData.setDeath("dead");
        assertEquals("dead", eventData.getDeath());
    }
}
