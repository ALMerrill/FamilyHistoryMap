package access;
import model.User;
import model.AuthToken;
import java.sql.*;


/** The UserDao class interacts with the database to insert, remove or modify Users in the database.
 *
 * @author Andrew Merrill
 * @version 1.0 May 18, 2017
 */
public class UserDao extends IDao{

    public UserDao(){

    }

    public UserDao(User user){}

    public boolean insertUser(User user, Connection connection) throws DatabaseException{
        PreparedStatement stmt = null;

        try {
            String sql = "INSERT INTO users VALUES (?, ?, ?, ?, ?, ?, ?)";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, String.valueOf(user.getGender()));
            stmt.setString(7, user.getPersonID());
            stmt.executeUpdate();
            System.out.println("Successfully added User");

        }
        catch (SQLException e) {
            System.out.println("SQL Exception");
            System.out.println("Failed to add User");
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

    public User getUser(String userName, Connection connection) {

        User result = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT userName, password, emailAddress, firstName, lastName, gender, personID " +
                    "FROM users WHERE  userName = " + "\"" + userName + "\"";
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String password = rs.getString(2);
                String emailAddress = rs.getString(3);
                String firstName = rs.getString(4);
                String lastName = rs.getString(5);
                char gender = rs.getString(6).charAt(0);
                String personID = rs.getString(7);
                result = new User(userName, password, emailAddress, firstName, lastName, gender, personID);
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

    /** Creates a new user account, generates 4 generations of ancestor data for the new
     user, logs the user in, and returns an auth token.
     *
     * @throws DatabaseException    Something didn't work when interacting with the database
     * @param u                     The user object containing all registration information.
     * @return			            The AuthToken that belongs to the user who successfully logged in.
     */
    public AuthToken register(User u, Connection connection) throws DatabaseException{
        String token = generateString(10);
        AuthToken result = null;
        if(existsUserName(u, connection)){
            return null;
        }
        String ID = generateString(9);
        u.setPersonID(ID);
        insertUser(u, connection);
        result = new AuthToken(token, ID, u.getUserName(), System.currentTimeMillis());
        return result;
    }

    /**  Logs in the user and returns an auth token.
     *
     * @throws DatabaseException    Something didn't work when interacting with the database
     * @param userName			The username of the user trying to login
     * @param password			The password that matches the user logging in.
     * @return			        The AuthToken that belongs to the user who successfully logged in.
     */
    public AuthToken login(String userName, String password, Connection connection) throws DatabaseException{
        String token = generateString(10);
        AuthToken result = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT userName, password, personID FROM users WHERE userName = " + "\"" + userName + "\"";
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            boolean existsUsername = false;
            while (rs.next()) {
                existsUsername = true;
                String userDB = rs.getString(1);
                String passDB = rs.getString(2);
                String personID = rs.getString(3);
                System.out.println("userDB: " + userDB);
                System.out.println("passDB: " + passDB);
                System.out.println("personIDDB: " + personID);
                System.out.println("username: " + userName);
                System.out.println("password: " + password);

                if(!password.equals(passDB)) {
                    System.out.println("Incorrect password");
                    connection.close();
                    return null;
                }
                if(password.equals(passDB) && existsUsername)
                    result = new AuthToken(token, personID, userName, System.currentTimeMillis());
            }
            if(!existsUsername){
                System.out.println("Username does not exist");
                connection.close();
                return null;
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

    public boolean existsUserName(User u, Connection connection){
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = null;

        try {
            sql = "SELECT userName FROM users WHERE userName = " + "\"" + u.getUserName() + "\"";
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            if(rs.next()){
                System.out.println("Username taken");
                return true;
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Exception");
            return true;
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
        return false;
    }

    /** Clears all users from the database.
     *
     * @throws DatabaseException    Something didn't work when interacting with the database
     */
    public boolean clearUsers(Connection connection) throws DatabaseException{
        PreparedStatement stmt = null;

        try {
            String sql = "DELETE FROM users";
            stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
            System.out.println("Successfully cleared users");

        }
        catch (SQLException e) {
            System.out.println("SQL Exception");
            System.out.println("Failed to clear users");
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
