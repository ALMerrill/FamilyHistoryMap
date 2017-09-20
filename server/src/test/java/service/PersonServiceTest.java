package service;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import access.Access;
import access.AuthTokenDao;
import access.EventDao;
import access.PersonDao;
import model.AuthToken;
import model.Event;
import model.Person;

import static org.junit.Assert.assertEquals;
/**
 * Created by Andrew1 on 5/31/17.
 */

public class PersonServiceTest {
    public PersonServiceTest(){}

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
    public void testPersonAndAllPeople(){
        PersonService pService = new PersonService();
        PersonDao pDao = new PersonDao();
        AuthTokenDao aTDao = new AuthTokenDao();
        Person person = new Person("id", "username", "Polly", "Grover", 'f', null, null, null);
        Person person2 = new Person("id2", "username", "Polly2", "Grover", 'f', null, null, null);
        AuthToken token = new AuthToken("token", "10", "username", 234098);
        pDao.insertPerson(person, connection);
        pDao.insertPerson(person2, connection);
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Person result = pService.person("id");
        assertEquals(person.getPerson(), result.getPerson());

        Vector<Person> vResult = pService.allPeople(token);
        assertEquals(person.getFirstName(), vResult.elementAt(0).getFirstName());
        assertEquals(person2.getPerson(), vResult.elementAt(1).getPerson());
    }

}
