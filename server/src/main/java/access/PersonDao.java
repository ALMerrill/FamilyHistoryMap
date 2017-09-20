package access;
import java.io.File;
import java.sql.*;
import java.util.Vector;

import model.Person;
import model.AuthToken;

/** The PersonDao class interacts with the database to insert, remove or modify People in the database.
 *
 * @author Andrew Merrill
 * @version 1.0 May 18, 2017
 */
public class PersonDao extends IDao{

    public PersonDao(){

    }

    public boolean insertPerson(Person person, Connection connection){
        PreparedStatement stmt = null;

        try {
            String sql = "INSERT INTO people VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, person.getPerson());
            stmt.setString(2, person.getDescendant());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, String.valueOf(person.getGender()));
            stmt.setString(6, person.getFather());
            stmt.setString(7, person.getMother());
            stmt.setString(8, person.getSpouse());
            stmt.executeUpdate();
            System.out.println("Successfully added person");

        }
        catch (SQLException e) {
            System.out.println("SQL Exception");
            System.out.println("Failed to add person");
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

    public Person getPerson(String personID, Connection connection) throws DatabaseException{
        Person result = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM people WHERE personID = " + "\"" + personID + "\"";
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString(1);
                String descendant = rs.getString(2);
                String firstName = rs.getString(3);
                String lastName = rs.getString(4);
                char gender = rs.getString(5).charAt(0);
                String fatherID = rs.getString(6);
                String motherID = rs.getString(7);
                String spouseID = rs.getString(8);
                result = new Person(id, descendant, firstName, lastName, gender, fatherID, motherID, spouseID);
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

    /** Retrieves all of the family members of the current user.
     *
     * @param descendant        descendant of the people to return
     * @throws DatabaseException    Something didn't work when interacting with the database.
     * @return			    An array of people, null if it does not work.
     */
    public Vector<Person> getAllPeople(String descendant, Connection connection) throws DatabaseException{
        Vector<Person> result = new Vector<Person>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM people WHERE descendant = " + "\"" + descendant + "\"";
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString(1);
                String descendantDB = rs.getString(2);
                String firstName = rs.getString(3);
                String lastName = rs.getString(4);
                char gender = rs.getString(5).charAt(0);
                String fatherID = rs.getString(6);
                String motherID = rs.getString(7);
                String spouseID = rs.getString(8);
                result.add(new Person(id, descendantDB, firstName, lastName, gender, fatherID, motherID, spouseID));
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

    /** Clears all people who are related to the descendant from the database.
     *
     * @param descendant    the username of the descendant of the people to erase.
     * @throws DatabaseException    Something didn't work when interacting with the database
     */
    public boolean clearUserPeople(String descendant, Connection connection){
        PreparedStatement stmt = null;

        try {
            String sql = "DELETE FROM people WHERE descendant =" + "\"" + descendant + "\"";
            stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
            System.out.println("Successfully cleared associated people");

        }
        catch (SQLException e) {
            System.out.println("SQL Exception");
            System.out.println("Failed to clear associated people");
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

    /** Clears all people from the database.
     *
     * @throws DatabaseException    Something didn't work when interacting with the database
     */
    public boolean clearPeople(Connection connection) throws DatabaseException{
        PreparedStatement stmt = null;

        try {
            String sql = "DELETE FROM people";
            stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
            System.out.println("Successfully cleared people");

        }
        catch (SQLException e) {
            System.out.println("SQL Exception");
            System.out.println("Failed to clear people");
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
