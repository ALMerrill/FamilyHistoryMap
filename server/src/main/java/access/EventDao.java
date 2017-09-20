package access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import model.Event;

/** The EventDao class interacts with the database to insert, remove or modify Events in the database.
 *
 * @author Andrew Merrill
 * @version 1.0 May 18, 2017
 */
public class EventDao extends IDao{

    public EventDao(){

    }


    public boolean insertEvent(Event event, Connection connection){
        PreparedStatement stmt = null;

        try {
            String sql = "INSERT INTO events VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getDescendant());
            stmt.setString(3, event.getPersonID());
            stmt.setString(4, event.getLatitude());
            stmt.setString(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();
            System.out.println("Successfully added event");

        }
        catch (SQLException e) {
            System.out.println("SQL Exception");
            System.out.println("Failed to add event");
            return false;
        }
        finally {
            if (stmt != null) try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /** Retrieves an event from the database.
     *
     * @param eventID               The event ID of the Person to retrieve.
     * @throws DatabaseException    Something didn't work when interacting with the database.
     * @return			            The Event object with the specified ID, null if it does not work.
     */
    public Event getEvent(String eventID, Connection connection) throws DatabaseException{
        Event result = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT eventID, descendant, personID, latitude, longitude, country, city, eventType, year " +
                            "FROM events WHERE eventID = " + "\"" + eventID + "\"";
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString(1);
                String descendant = rs.getString(2);
                String personID = rs.getString(3);
                String latitude = rs.getString(4);
                String longitude = rs.getString(5);
                String country = rs.getString(6);
                String city = rs.getString(7);
                String eventType = rs.getString(8);
                int year = rs.getInt(9);
                result = new Event(id, descendant, personID, latitude, longitude, country, city, eventType, year);
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception");
            return null;
        }
        finally {
            if (rs != null) try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (stmt != null) try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /** Retrieves all of the events of all family members of the current user.
     *
     * @param descendant        descendant of the events to return
     * @throws DatabaseException    Something didn't work when interacting with the database.
     * @return			    An array of events from all of the users family, null if it does not work.
     */
    public Vector<Event> getAllEvents(String descendant, Connection connection) throws DatabaseException{
        Vector<Event> result = new Vector<Event>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM events WHERE descendant = " + "\"" + descendant + "\"";
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString(1);
                String descendantDB = rs.getString(2);
                String personID = rs.getString(3);
                String latitude = rs.getString(4);
                String longitude = rs.getString(5);
                String country = rs.getString(6);
                String city = rs.getString(7);
                String eventType = rs.getString(8);
                int year = rs.getInt(9);
                result.add(new Event(id, descendantDB, personID, latitude, longitude, country, city, eventType, year));
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception");
            return null;
        }
        finally {
            if (rs != null) try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (stmt != null) try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /** Clears all events of all the people who are related to the descendant from the database.
     *
     * @param descendant    the username of the descendant of the events to erase.
     * @throws DatabaseException    Something didn't work when interacting with the database
     */
    public boolean clearUserEvents(String descendant, Connection connection){
        PreparedStatement stmt = null;

        try {
            String sql = "DELETE FROM events WHERE descendant =" + "\"" + descendant + "\"";
            stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
            System.out.println("Successfully cleared associated events");

        }
        catch (SQLException e) {
            System.out.println("SQL Exception");
            System.out.println("Failed to clear associated events");
            return false;
        }
        finally {
            if (stmt != null) try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    /** Clears all events from the database.
     *
     * @throws DatabaseException    Something didn't work when interacting with the database
     */
    public boolean clearEvents(Connection connection) throws DatabaseException{
        PreparedStatement stmt = null;

        try {
            String sql = "DELETE FROM events";
            stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
            System.out.println("Successfully cleared events");

        }
        catch (SQLException e) {
            System.out.println("SQL Exception");
            System.out.println("Failed to clear events");
            return false;
        }
        finally {
            if (stmt != null) try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

}
