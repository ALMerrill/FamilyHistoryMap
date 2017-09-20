package model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Andrew1 on 5/31/17.
 */

public class UserTest {
    @Test
    public void testGetsAndSets(){
        User user = new User("", "", "", "" , "", 'm', "");
        user.setUserName("user");
        assertEquals("user", user.getUserName());
        user.setPersonID("person");
        assertEquals("person", user.getPersonID());
        user.setFirstName("first");
        assertEquals("first", user.getFirstName());
        user.setLastName("last");
        assertEquals("last", user.getLastName());
        user.setGender('f');
        assertEquals('f', user.getGender());
        user.setPassword("pass");
        assertEquals("pass", user.getPassword());
        user.setEmail("email");
        assertEquals("email", user.getEmail());
    }
}
