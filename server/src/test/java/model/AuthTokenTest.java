package model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Andrew1 on 5/31/17.
 */

public class AuthTokenTest {
    @Test
    public void testGetsAndSets(){
        AuthToken AuthToken = new AuthToken("", "", "", 0);
        AuthToken.setToken("AuthToken");
        assertEquals("AuthToken", AuthToken.getToken());
        AuthToken.setPersonID("personID");
        assertEquals("personID", AuthToken.getPersonID());
        AuthToken.setUserName("user");
        assertEquals("user", AuthToken.getUserName());
        AuthToken.setTimeStamp(1);
        assertEquals(1, AuthToken.getTimeStamp());
    }
}
