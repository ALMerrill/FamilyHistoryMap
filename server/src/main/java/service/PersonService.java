package service;

import java.sql.Connection;
import java.util.Vector;

import access.Access;
import access.IDao;
import access.PersonDao;
import model.AuthToken;
import model.Event;
import model.Person;

/** The PersonService class uses the PersonDao to retrieve people from tha database.
 *
 * @author Andrew Merrill
 * @version 1.0 May 18, 2017
 */
public class PersonService {

    public PersonService(){}

    /** Utilizes the PersonDao to retrieve a person from the database.
     *
     * @param personID     The person ID of the Person to retrieve.
     * @return			    The Person object with the specified ID, null if it does not work.
     */
    public Person person(String personID){
        PersonDao pDao = new PersonDao();
        Person person = null;
        Access access = new Access();
        Connection connection = access.connect();
        boolean allDone = true;
        try {
            person = pDao.getPerson(personID, connection);
            if(person == null)
                allDone = false;
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }
        access.commitRoll(allDone);
        return person;
    }

    /** Utilizes the PersonDao to retrieve all family members of the current user.
     *
     * @param token         AuthToken to determine the current user.
     * @return			    An array of people in the users family, null if it does not work.
     */
    public Vector<Person> allPeople(AuthToken token){
        PersonDao pDao = new PersonDao();
        String descendant = token.getUserName();
        Vector<Person> people = new Vector<Person>();
        Access access = new Access();
        Connection connection = access.connect();
        boolean allDone = true;
        try {
            people = pDao.getAllPeople(descendant, connection);
            if(people == null)
                allDone = false;
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }
        access.commitRoll(allDone);
        return people;
    }
}
