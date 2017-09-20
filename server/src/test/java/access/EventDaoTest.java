package access;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import access.IDao;
import access.EventDao;
import model.AuthToken;
import model.Event;
import service.ClearService;

import static org.junit.Assert.assertEquals;

/**
 * Created by Andrew1 on 5/24/17.
 */

public class EventDaoTest {
    private Access access;
    private Connection connection;
    private EventDao eDao;

    @Before
    public void setUp(){
        access = new Access();
        connection = access.connect();
        eDao = new EventDao();
    }

    @After
    public void tearDown(){
        ClearService service = new ClearService();
        service.clear(connection);
        access.commitRoll(true);
    }

    @Test
    public void testInsertAndGetEvent(){
        Event event = new Event("1", "username", "1", "1.5", "1.5", "USA", "Provo", "Birth", 1995);

        eDao.insertEvent(event, connection);
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Event compare = null;
        try {
            compare = eDao.getEvent("1", connection);
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }

        assertEquals(event.getEventID(), compare.getEventID());
        assertEquals(event.getPersonID(), compare.getPersonID());
        assertEquals(event.getEventType(), compare.getEventType());
        assertEquals(event.getYear(), compare.getYear());
    }

    @Test
    public void testGetAllEvents(){
        Event event = new Event("1", "username", "1", "1.5", "1.5", "USA", "Provo", "Birth", 1995);
        Event event2 = new Event("2", "username", "1", "1.5", "1.5", "USA", "Provo", "Death", 2000);
        Event event3 = new Event("3", "username", "1", "1.5", "1.5", "USA", "Provo", "Exaltation", 2001);

        eDao.insertEvent(event, connection);
        eDao.insertEvent(event2, connection);
        eDao.insertEvent(event3, connection);
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Vector<Event> compare = new Vector<Event>();
        try {
            compare = eDao.getAllEvents("username", connection);
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }

        assertEquals(event.getEventID(), compare.elementAt(0).getEventID());
        assertEquals(event2.getPersonID(), compare.elementAt(1).getPersonID());
        assertEquals(event3.getEventType(), compare.elementAt(2).getEventType());
    }

    @Test
    public void testClearUserEvents(){
        Event event = new Event("1", "username", "1", "1.5", "1.5", "USA", "Provo", "Birth", 1995);
        Event event2 = new Event("2", "username", "1", "1.5", "1.5", "USA", "Provo", "Death", 2000);
        Event event3 = new Event("3", "OTHER", "1", "1.5", "1.5", "USA", "Provo", "Exaltation", 2001);

        eDao.insertEvent(event, connection);
        eDao.insertEvent(event2, connection);
        eDao.insertEvent(event3, connection);
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        eDao.clearUserEvents("username", connection);

        Vector<Event> compare = new Vector<Event>();
        try {
            compare = eDao.getAllEvents("username", connection);
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }
        assertEquals(compare.elementAt(0).getEventID(), null);
        assertEquals(compare.elementAt(1).getEventID(), null);
        Event comp = null;
        try {
            comp = eDao.getEvent("OTHER", connection);
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }

        assertEquals(event3.getPersonID(), comp.getPersonID());
        assertEquals(event3.getEventType(), comp.getEventType());
    }

    @Test
    public void testClearEvents(){
        Event event = new Event("1", "username", "1", "1.5", "1.5", "USA", "Provo", "Birth", 1995);
        Event event2 = new Event("2", "username", "1", "1.5", "1.5", "USA", "Provo", "Death", 2000);

        eDao.insertEvent(event, connection);
        eDao.insertEvent(event2, connection);
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            eDao.clearEvents(connection);
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }

        Event comp = null;
        Event comp2 = null;
        try {
            comp = eDao.getEvent("1", connection);
            comp2 = eDao.getEvent("2", connection);
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }
        assertEquals(comp, null);
        assertEquals(comp2, null);

    }
}
