package service;

import java.sql.Connection;

import access.*;
import model.Event;
import model.Person;
import model.User;
import request.LoadRequest;
import result.LoadResult;

/** The LoadService class uses the Access classes and the Model classes to clear all data from the database, and refill it with specific User, Person, and Event data.
 *
 * @author Andrew Merrill
 * @version 1.0 May 18, 2017
 */
public class LoadService {

    public LoadService(){

    }

    /**  Clears all data from the database, and then loads the posted user, person, and event data into the database.
     *
     * @param r             The load request containing an array of users , and array of people and an array of events to be loaded into the database.
     * @return              Load result containing a success or error message.
     */
    public LoadResult load(LoadRequest r, Connection connection){
        UserDao uDao = new UserDao();
        PersonDao pDao = new PersonDao();
        EventDao eDao = new EventDao();
        User[] users = r.getUsers();
        Person[] persons = r.getPersons();
        Event[] events = r.getEvents();
        int numUsers = 0;
        for(User u: users){
            if(u != null)
                numUsers++;
        }
        int numPersons = 0;
        for(Person p: persons) {
            if (p != null)
                numPersons++;
        }
        int numEvents = 0;
        for(Event e: events){
            if(e != null)
                numEvents++;
        }
        LoadResult result = null;
        try {
            for(int i = 0; i < numUsers; i++){
                if(!uDao.insertUser(users[i], connection)){
                    result = new LoadResult("Failed to load users");
                    return result;
                }
            }
            for(int i = 0; i < numPersons; i++){
                if(!pDao.insertPerson(persons[i], connection)){
                    result = new LoadResult("Failed to load persons");
                    return result;
                }
            }
            for(int i = 0; i < numEvents; i++){
                if(!eDao.insertEvent(events[i], connection)){
                    result = new LoadResult("Failed to load events");
                    return result;
                }
            }
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
            result = new LoadResult("Failed to load the data");
            return result;
        }
        result = new LoadResult(numUsers, numPersons, numEvents);
        return result;
    }

}
