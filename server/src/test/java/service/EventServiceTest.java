package service;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import static org.junit.Assert.assertEquals;

import access.Access;
import access.AuthTokenDao;
import model.AuthToken;
import service.EventService;
import model.Event;
import access.EventDao;
import static org.junit.Assert.assertEquals;

/**
 * Created by Andrew1 on 5/25/17.
 */

public class EventServiceTest {
    public EventServiceTest(){}

    private Access access;
    private Connection connection;

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
    public void testEventAndAllEvents(){
        EventService eService = new EventService();
        EventDao eDao = new EventDao();
        AuthTokenDao aTDao = new AuthTokenDao();
        Event event = new Event("1", "username", "10", "1.5", "1.5", "USA", "Provo", "Birth", 1995);
        Event event2 = new Event("2", "username", "10", "1.5", "1.5", "USA", "Provo", "Finished This Project", 3000);
        AuthToken token = new AuthToken("token", "10", "username", 234098);
        eDao.insertEvent(event, connection);
        eDao.insertEvent(event2, connection);
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Event result = eService.event("1");
        assertEquals(event.getPersonID(), result.getPersonID());

        Vector<Event> vResult = eService.allEvents(token);
        assertEquals(event.getEventID(), vResult.elementAt(0).getEventID());
        assertEquals(event2.getPersonID(), vResult.elementAt(1).getPersonID());
    }


}
