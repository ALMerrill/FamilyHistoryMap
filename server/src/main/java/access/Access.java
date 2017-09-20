package access;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** The Access class creates and holds a connection to the database, so that there is only one
 *
 * @author Andrew Merrill
 * @version 1.0 May 18, 2017
 */
public class Access {
    private Connection connection;

    public Connection connect(){
        try {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.out.println("Class Not Found Exception");
            e.printStackTrace();
            return null;
        }

        String dbName = "database" + File.separator + "FamilyMap.db";
        String connectionURL = "jdbc:sqlite:" + dbName;
        connection = null;

        try {
            // Open a database connection
            connection = DriverManager.getConnection(connectionURL);
            // Start a transaction
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            System.out.println("SQL Exception");
            e.printStackTrace();
            return null;
        }
        return connection;
    }

    public void commitRoll(boolean result) {
        try {
            if (result) {
                connection.commit();
            }
            else {
                connection.rollback();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        connection = null;
    }
}
