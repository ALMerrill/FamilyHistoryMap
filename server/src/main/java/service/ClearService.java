package service;
import java.sql.Connection;

import access.*;
import result.ClearResult;

/** The ClearService class uses the Access classes and the Model classes to clear data from the database.
 *
 * @author Andrew Merrill
 * @version 1.0 May 18, 2017
 */
public class ClearService {

    public ClearService(){

    }

    /** Clears all data from the database, utilizing all of the Dao classes.
     *
     * @return      The clear result containing a success or error message.
     */
    public ClearResult clear(Connection connection){
        ClearResult result = new ClearResult();
        AuthTokenDao atDao = new AuthTokenDao();
        EventDao eDao = new EventDao();
        PersonDao pDao = new PersonDao();
        UserDao uDao = new UserDao();

        try {
            atDao.clearAuthTokens(connection);
            eDao.clearEvents(connection);
            pDao.clearPeople(connection);
            uDao.clearUsers(connection);
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
            result.setMessage("Unable to clear data");
            return result;
        }
        result.setMessage("Clear succeeded");
        return result;
    }
}
