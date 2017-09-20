package service;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import access.Access;
import access.AuthTokenDao;
import access.IDao;
import access.UserDao;
import model.AuthToken;
import model.User;
import request.RegisterRequest;
import result.LoginResult;
import service.LoginService;
import request.LoginRequest;
import static org.junit.Assert.assertEquals;
/**
 * Created by Andrew1 on 5/23/17.
 */

public class LoginServiceAndRegisterServiceTest {
    private Access access;
    private Connection connection;

    @Before
    public void setUp(){
        access = new Access();
        connection = access.connect();
    }

    @After
    public void tearDown(){
        ClearService service = new ClearService();
        service.clear(connection);
        access.commitRoll(true);
    }

    @Test
    public void testRegisterAndLogin(){
        User input = new User("user", "pass", "email", "Polly", "Grover", 'f', "id");
        UserDao uDao = new UserDao();
        AuthTokenDao aTDao = new AuthTokenDao();
        RegisterService rService = new RegisterService();
        LoginService lService = new LoginService();
        RegisterRequest rRequest = new RegisterRequest("user", "pass", "email", "first", "last", 'm');
        LoginRequest lRequest = new LoginRequest("user", "pass");
        AuthToken token = null;

        rService.register(rRequest);

        User compare = uDao.getUser("user", connection);
        assertEquals(input.getGender(), compare.getGender());
        assertEquals(input.getFirstName(), compare.getFirstName());

        LoginResult result = lService.login(lRequest);

        AuthToken comp = aTDao.getAuthToken(result.getToken(), connection);
        assertEquals(result.getToken(), comp.getToken());
        assertEquals(result.getPersonID(), comp.getPersonID());
    }
}
