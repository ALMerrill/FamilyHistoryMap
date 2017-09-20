package service;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import access.Access;
import access.EventDao;
import access.IDao;
import access.PersonDao;
import model.Event;
import model.Person;
import request.FillRequest;
import result.FillResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
/**
 * Created by Andrew1 on 5/27/17.
 */

public class FillServiceTest {
    public FillServiceTest(){}

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
    public void testFill(){
        FillService service = new FillService();
        PersonDao pDao = new PersonDao();
        Person test = service.createPerson("id", 'm', "username", "Merrill");
        FillRequest request = new FillRequest("username", 4);
        service.fill(request);

        Vector<Person> vec = null;
        try {
            vec = pDao.getAllPeople("username", connection);
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }

        assertEquals(vec.size(), 31); //number of people added in 3 generations.
    }

    @Test
    public void testCreatePerson(){
        FillService service = new FillService();
        Person created = service.createPerson("id1", 'm', "username", null);
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        PersonDao pDao = new PersonDao();
        pDao.insertPerson(created, connection);
        Person result = null;
        try {
            result = pDao.getPerson("id1", connection);
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }
        assertEquals(result.getPerson(), "id1");
        assertEquals(result.getGender(), 'm');
        assertEquals(result.getDescendant(), "username");
    }

    @Test
    public void testCreateEvents(){
        FillService service = new FillService();
        EventDao eDao = new EventDao();
        String[] eventIDs = {null, null, null, null};
        for(int i = 0; i < 4; i++){
            eventIDs[i] = eDao.generateString(11); //length of eventIDs
        }
        service.createEvents("id", eventIDs, "username", 2000, connection);
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Event result = eDao.getEvent(eventIDs[0], connection);
            assertEquals(result.getEventType(), "Birth");
            assertEquals(result.getDescendant(), "username");
            Event shouldBeNull = eDao.getEvent(eventIDs[3], connection); //death that should not have been made.
            assertEquals(shouldBeNull, null);
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < 4; i++){
            eventIDs[i] = eDao.generateString(11); //length of eventIDs
        }
        service.createEvents("id2", eventIDs, "username", 1900, connection);
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            Event result = eDao.getEvent(eventIDs[3], connection);
            assertEquals(result.getEventType(), "Death");
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateGeneration(){
        FillService service = new FillService();
        PersonDao pDao = new PersonDao();
        int genNum = 2;
        int size = (int)Math.pow(2,genNum + 1) - 1;
        Person[] allPeople = new Person[size];
        Person[] prevGen = {service.createPerson("zxcvbnm12", 'm', "username", "Merrill"), service.createPerson("asdfghjkl", 'f', "username", null)};
        allPeople[0] = service.createPerson("qwertyuio", 'm', "username", "Merrill");
        int j = 0;
        for(int i = 1; i <  1 + prevGen.length; i++){
            allPeople[i] = prevGen[j];
            j++;
        }
        Person[] curGen = service.createGeneration(prevGen, 2);
        int k = 0;
        for(int i = 3; i <  allPeople.length; i++){
            allPeople[i] = curGen[k];
            k++;
        }
        for(int i = 0; i < allPeople.length - (Math.pow(2,genNum)); i++){   //all people made except last generation
            allPeople[i].setFather(allPeople[(i * 2) + 1].getPerson()); //personID of person at index of father.
            allPeople[i].setMother(allPeople[(i * 2) + 2].getPerson()); //personID of perons at index of mother.
        }
        for(int i = 1; i < allPeople.length; i++) {   //everyone but the root person, who is not married
            if(i % 2 == 1 && i != 0)    //index of male that is not first person in "tree" array (who is not married)
                allPeople[i].setSpouse(allPeople[i + 1].getPerson());
            else if(i != 0)
                allPeople[i].setSpouse(allPeople[i - 1].getPerson());
        }
        for(int i = 0; i < allPeople.length; i++){
            pDao.insertPerson(allPeople[i], connection);
        }
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Person father1 = null;
        try {
            father1 = pDao.getPerson(allPeople[3].getPerson(), connection);
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }
        Person mother1 = null;
        try {
            mother1 = pDao.getPerson(allPeople[4].getPerson(), connection);
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }

        assertEquals(allPeople[1].getFather(), father1.getPerson());
        assertEquals(allPeople[1].getMother(), mother1.getPerson());
        assertEquals(father1.getSpouse(), mother1.getPerson());
        assertEquals(mother1.getSpouse(), father1.getPerson());
        assertEquals(father1.getDescendant(), mother1.getDescendant());

    }

    @Test
    public void testCreateAllGenerations(){
        FillService service = new FillService();
        PersonDao pDao = new PersonDao();
        Person test = service.createPerson("id", 'm', "username", "Merrill");
        service.createAllGenerations(test, 3, connection);
        Vector<Person> vec = null;
        try {
            vec = pDao.getAllPeople("username", connection);
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }

        assertEquals(vec.size(), 15); //number of people added in 3 generations.
    }
}
