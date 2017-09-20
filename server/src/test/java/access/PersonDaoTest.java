package access;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import access.IDao;
import access.PersonDao;
import model.Event;
import model.Person;
import service.ClearService;

import static org.junit.Assert.assertEquals;

public class PersonDaoTest {
    private Access access;
    private Connection connection;
    private PersonDao pDao;

    @Before
    public void setUp(){
        access = new Access();
        connection = access.connect();
        pDao = new PersonDao();
    }

    @After
    public void tearDown(){
        ClearService service = new ClearService();
        service.clear(connection);
        access.commitRoll(true);
    }

    @Test
    public void testInsertAndGetPerson(){
        Person input = new Person("id", "username", "Polly", "Grover", 'f', null, null, null);
        pDao.insertPerson(input, connection);
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Person result = null;
        try {
            result = pDao.getPerson("id", connection);
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }


        assertEquals(input.getFather(), result.getFather());
        System.out.println(input.getFather());
        assertEquals(input.getFirstName(), result.getFirstName());
        assertEquals(input.getDescendant(), result.getDescendant());
    }

    @Test
    public void testGetAllPeople(){
        Person person = new Person("id", "username", "Polly", "Grover", 'f', null, null, null);
        Person person2 = new Person("id2", "username", "Polly2", "Grover", 'f', null, null, null);
        Person person3 = new Person("id3", "username", "Polly3", "Grover", 'f', null, null, null);

        pDao.insertPerson(person, connection);
        pDao.insertPerson(person2, connection);
        pDao.insertPerson(person3, connection);
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Vector<Person> compare = new Vector<Person>();
        try {
            compare = pDao.getAllPeople("username", connection);
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }

        assertEquals(person.getPerson(), compare.elementAt(0).getPerson());
        assertEquals(person2.getFirstName(), compare.elementAt(1).getFirstName());
        assertEquals(person3.getGender(), compare.elementAt(2).getGender());
    }

    @Test
    public void testClearUserPeople(){
        Person person = new Person("id", "username", "Polly", "Grover", 'f', null, null, null);
        Person person2 = new Person("id2", "username", "Polly2", "Grover", 'f', null, null, null);
        Person person3 = new Person("id3", "username", "Polly3", "Grover", 'f', null, null, null);

        pDao.insertPerson(person, connection);
        pDao.insertPerson(person2, connection);
        pDao.insertPerson(person3, connection);
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pDao.clearUserPeople("username", connection);

        Vector<Person> compare = new Vector<Person>();
        try {
            compare = pDao.getAllPeople("username", connection);
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }
        assertEquals(compare.elementAt(0).getPerson(), null);
        assertEquals(compare.elementAt(1).getPerson(), null);
        Person comp = null;
        try {
            comp = pDao.getPerson("OTHER", connection);
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }

        assertEquals(person3.getPerson(), comp.getPerson());
        assertEquals(person3.getFirstName(), comp.getFirstName());
    }

    @Test
    public void testClearPeople(){
        Person person = new Person("id", "username", "Polly", "Grover", 'f', null, null, null);
        Person person2 = new Person("id2", "username", "Polly2", "Grover", 'f', null, null, null);

        pDao.insertPerson(person, connection);
        pDao.insertPerson(person2, connection);
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            pDao.clearPeople(connection);
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }

        Person comp = null;
        Person comp2 = null;
        try {
            comp = pDao.getPerson("id1", connection);
            comp2 = pDao.getPerson("id2", connection);
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }
        assertEquals(comp, null);
        assertEquals(comp2, null);

    }
}
