package service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.Vector;

import access.Access;
import access.EventDao;
import access.IDao;
import access.PersonDao;
import model.Event;
import model.Person;
import model.User;
import request.FillRequest;
import request.LoadRequest;

import static org.junit.Assert.assertEquals;

/**
 * Created by Andrew1 on 5/31/17.
 */

public class LoadServiceTest {

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
    public void testLoad() {
        LoadService service = new LoadService();
        PersonDao pDao = new PersonDao();
        EventDao eDao = new EventDao();
        Event event = new Event("1", "username", "1", "1.5", "1.5", "USA", "Provo", "Birth", 1995);
        Event event2 = new Event("2", "username", "1", "1.5", "1.5", "USA", "Provo", "Death", 2000);
        Event event3 = new Event("3", "username", "1", "1.5", "1.5", "USA", "Provo", "Exaltation", 2001);
        Person person = new Person("id", "username", "Polly", "Grover", 'f', null, null, null);
        Person person2 = new Person("id2", "username", "Polly2", "Grover", 'f', null, null, null);
        Person person3 = new Person("id3", "username", "Polly3", "Grover", 'f', null, null, null);
        User user = new User("user", "pass", "email", "Polly", "Grover", 'f', "id");
        User user2 = new User("user2", "pass2", "email", "Polly2", "Grover", 'f', "id2");
        Event[] events = {event, event2, event3};
        Person[] persons = {person, person2, person3};
        User[] users = {user, user2};


        LoadRequest request = new LoadRequest(users, persons, events);
        service.load(request, connection);

        Vector<Person> vec = null;
        Vector<Event> vec2 = null;
        try
        {
            vec = pDao.getAllPeople("username", connection);
            vec2 = eDao.getAllEvents("username", connection);
        } catch(IDao.DatabaseException e)
        {
            e.printStackTrace();
        }

        assertEquals(vec.size(), 3);
        assertEquals(vec2.size(), 3);
    }


}
