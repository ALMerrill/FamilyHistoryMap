package service;
import java.sql.Connection;

import access.Access;
import model.AuthToken;
import access.IDao;
import access.UserDao;
import access.AuthTokenDao;
import result.LoginResult;
import request.LoginRequest;

/** The LoginService class uses the Access classes and the Model classes to login a User into Family Map.
 *
 * @author Andrew Merrill
 * @version 1.0 May 18, 2017
 */
public class LoginService {

    public LoginService(){

    }

    /**  Utilizes the UserDao to login the user.
     *
     * @param request			        Login request holding The username and password of the user trying to login
     * @return			        The login result containing the AuthToken, username and person ID.
     */
    public LoginResult login(LoginRequest request){
        LoginResult result = null;
        UserDao uDao = new UserDao();
        AuthTokenDao aTDao = new AuthTokenDao();
        AuthToken token = null;
        Access access = new Access();
        Connection connection = access.connect();
        boolean allDone = true;

        try {
            token = uDao.login(request.getUsername(), request.getPassword(), connection);
            if(token != null) {
                if(!aTDao.insertAuthToken(token, connection))
                    allDone = false;
            }
            else
                allDone = false;
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }
        if(token == null)
            result = new LoginResult("Incorrect username or password");
        else
            result = new LoginResult(token.getToken(),token.getUserName(), token.getPersonID());
        access.commitRoll(allDone);
        return result;
    }
}
