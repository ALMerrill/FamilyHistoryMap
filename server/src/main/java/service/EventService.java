package service;

import java.sql.Connection;
import java.util.Vector;

import access.Access;
import access.EventDao;
import access.IDao;
import access.PersonDao;
import model.AuthToken;
import model.Event;

/** The EventService class uses the EventDao to retrieve events from tha database.
 *
 * @author Andrew Merrill
 * @version 1.0 May 18, 2017
 */
public class EventService {
    //private EventDao eD;

    public EventService() {}

    /** Utilizes the EventDao to retrieve an event from the database.
     *
     * @param event_id      The event ID of the Person to retrieve.
     * @return			    The Event object with the specified ID, null if it does not work.
     */
    public Event event(String event_id){
        EventDao eDao = new EventDao();
        Event event = null;
        Access access = new Access();
        Connection connection = access.connect();
        boolean allDone = true;
        try {
            event = eDao.getEvent(event_id, connection);
            if(event == null)
                allDone = false;
            System.out.println(event.getEventType());
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }
        access.commitRoll(allDone);
        return event;
    }

    /** Utilizes the EventDao to retrieve all of the events of all family members of the current user.
     *
     * @param token         AuthToken to determine the current user.
     * @return			    An array of events from all of the users family, null if it does not work.
     */
    public Vector<Event> allEvents(AuthToken token){
        EventDao eDao = new EventDao();
        String descendant = token.getUserName();
        Vector<Event> events = new Vector<Event>();
        Access access = new Access();
        Connection connection = access.connect();
        boolean allDone = true;
        try {
            events = eDao.getAllEvents(descendant, connection);
            if(events == null)
                allDone = false;
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }
        access.commitRoll(allDone);
        return events;
    }

}
