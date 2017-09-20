package service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import access.Access;
import access.*;
import access.IDao;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import static org.junit.Assert.assertEquals;

/**
 * Created by Andrew1 on 5/31/17.
 */

public class ClearServiceTest {
    private Access access;
    private Connection connection;
    AuthTokenDao aTDao = new AuthTokenDao();
    EventDao eDao = new EventDao();
    PersonDao pDao = new PersonDao();
    UserDao uDao = new UserDao();

    @Before
    public void setUp(){
        access = new Access();
        connection = access.connect();
    }

    @After
    public void tearDown(){
        ClearService service = new ClearService();
        service.clear(connection);
        access.commitRoll(true);
    }

    @Test
    public void testClear(){
        AuthToken token = new AuthToken("lkfnqonaakfni", "1", "user", 234098);
        Event event = new Event("1", "username", "1", "1.5", "1.5", "USA", "Provo", "Birth", 1995);
        Person person = new Person("id", "username", "Polly", "Grover", 'f', null, null, null);
        User user = new User("user", "pass", "email", "Polly", "Grover", 'f', "id");

        try {
            aTDao.insertAuthToken(token, connection);
            eDao.insertEvent(event, connection);
            pDao.insertPerson(person, connection);
            uDao.insertUser(user, connection);
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ClearService service = new ClearService();
        service.clear(connection);


        AuthToken compT = null;
        Event eventT = null;
        Person personT = null;
        User userT = null;
        try {
            compT = aTDao.getAuthToken(token.getToken(), connection);
            eventT = eDao.getEvent(event.getEventID(), connection);
            personT = pDao.getPerson(person.getPerson(), connection);
            userT = uDao.getUser(user.getUserName(), connection);
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }

        assertEquals(compT, null);
        assertEquals(eventT, null);
        assertEquals(personT, null);
        assertEquals(userT, null);

    }
}
