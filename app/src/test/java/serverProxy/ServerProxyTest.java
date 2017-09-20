package serverProxy;

import net.ServerProxy;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.net.MalformedURLException;

import model.Event;
import model.Person;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;

/**
 * Created by Andrew1 on 6/5/17.
 */

public class ServerProxyTest {

    @Test
    public void testRegister() {
        RegisterRequest rRequest = new RegisterRequest("username", "password", "email", "Drew", "Ster", 'm');
        ServerProxy proxy = new ServerProxy("localhost", 8080);
        RegisterResult rResult = null;
        try {
            rResult = proxy.register(rRequest);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        assertEquals(rResult.getUsername(), "username");
    }

    @Test
    public void testLogin() {
        LoginRequest lRequest = new LoginRequest("username", "password");
        ServerProxy proxy = new ServerProxy("localhost", 8080);
        LoginResult lResult = null;
        try {
            lResult = proxy.login(lRequest);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        assertEquals(lResult.getUsername(), "username");
    }

    @Test
    public void testGetAllData() {
        RegisterRequest rRequest = new RegisterRequest("username", "password", "email", "Drew", "Ster", 'm');
        ServerProxy proxy = new ServerProxy("localhost", 8080);
        RegisterResult rResult = null;
        Person[] people = null;
        Event[] events = null;
        try {
            rResult = proxy.register(rRequest);
            String authToken = rResult.getToken();
            people = (Person[]) proxy.getAllData(authToken, "person");
            events = (Event[]) proxy.getAllData(authToken, "event");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        assertEquals(people[10].getDescendant(), "username"); //Everyone's descendant should be "username"
        assertEquals(people[0].getFirstName(), "Drew"); //Drew Ster
        assertEquals(people[1].getLastName(), "Ster");  //Drew Ster's father.
        assertEquals(events[10].getDescendant(), "username");

    }


}
