package access;
import model.AuthToken;
import java.sql.*;
import java.io.File;

import javax.xml.crypto.Data;


/** The AuthTokenDao class interacts with the database to insert, remove or modify AuthTokens in the database.
 *
 * @author Andrew Merrill
 * @version 1.0 May 18, 2017
 */
public class AuthTokenDao extends IDao{

    public AuthTokenDao(){

    }

    public boolean insertAuthToken(AuthToken token, Connection connection) throws DatabaseException{

        PreparedStatement stmt = null;

        try {
            String sql = "INSERT INTO authTokens (personID, userName, token, timeStamp) VALUES (?, ?, ?, ?)";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, token.getPersonID());
            stmt.setString(2, token.getUserName());
            stmt.setString(3, token.getToken());
            stmt.setLong(4, token.getTimeStamp());

            stmt.executeUpdate();
            System.out.println("Successfully added AuthToken");

        }
        catch (SQLException e) {
            System.out.println("SQL Exception");
            System.out.println("Failed to add AuthToken");
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

    public AuthToken getAuthToken(String token, Connection connection){
        AuthToken result = null;

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT personID, userName, token, timeStamp FROM authTokens WHERE token = " + "\"" + token + "\"";
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            boolean existsAuthToken = false;
            while (rs.next()) {
                existsAuthToken = true;
                String personID = rs.getString(1);
                String userName = rs.getString(2);
                String tokenDB = rs.getString(3);
                long timeStamp = rs.getLong(4);
                result = new AuthToken(tokenDB, personID, userName, timeStamp);
            }
            if(!existsAuthToken){
                System.out.println("AuthToken does not exist");
                return null;
            }

        }
        catch (SQLException e) {
            System.out.println("SQL Exception");
            System.out.println("Failed to get AuthToken");
            return null;
        }
        finally {
            if (stmt != null) try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return result;
    }

    /** Clears all AuthTokens that are expired from the database.
     *
     * @param expire        the amount of time that an AuthToken is good. After this time, it is expired.
     * @throws DatabaseException    Something didn't work when interacting with the database
     */
    public boolean clearExpiredAuthTokens(int expire, Connection connection) throws DatabaseException{
        PreparedStatement stmt = null;

        try {
            String sql = "DELETE FROM authTokens WHERE (" + Long.toString(System.currentTimeMillis()) +
                            " - timeStamp) > " + Integer.toString(expire * 1000);
            stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
            System.out.println("Successfully cleared expired AuthTokens");

        }
        catch (SQLException e) {
            System.out.println("SQL Exception");
            System.out.println("Failed to clear expired AuthTokens");
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

    /** Clears all AuthTokens from the database.
     *
     * @throws DatabaseException    Something didn't work when interacting with the database
     */
    public boolean clearAuthTokens(Connection connection) throws DatabaseException{

        PreparedStatement stmt = null;

        try {
            String sql = "DELETE FROM authTokens";
            stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
            System.out.println("Successfully cleared AuthTokens");

        }
        catch (SQLException e) {
            System.out.println("SQL Exception");
            System.out.println("Failed to clear AuthTokens");
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
