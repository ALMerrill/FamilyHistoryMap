package access;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import access.IDao;
import access.UserDao;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import service.ClearService;

import static org.junit.Assert.assertEquals;

public class UserDaoTest {
    private Access access;
    private Connection connection;
    private UserDao uDao;

    public UserDaoTest(){}

    @Before
    public void setUp(){
        access = new Access();
        connection = access.connect();
        uDao = new UserDao();
    }

    @After
    public void tearDown(){
        ClearService service = new ClearService();
        service.clear(connection);
        access.commitRoll(true);
    }

    @Test
    public void testInsertAndGetUser(){
        User input = new User("user", "pass", "email", "Polly", "Grover", 'f', "id");
        try {
            uDao.insertUser(input, connection);
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        User result = null;
        result = uDao.getUser("id", connection);

        assertEquals(input.getGender(), result.getGender());
        assertEquals(input.getFirstName(), result.getFirstName());
    }

    @Test
    public void testRegisterAndLogin(){
        User input = new User("user", "pass", "email", "Polly", "Grover", 'f', "id");
        UserDao uDao = new UserDao();
        AuthTokenDao aTDao = new AuthTokenDao();
        AuthToken token = null;
        try {
            uDao.register(input, connection);
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        User compare = uDao.getUser("user", connection);
        assertEquals(input.getGender(), compare.getGender());
        assertEquals(input.getFirstName(), compare.getFirstName());

        try {
            token = uDao.login("user", "pass", connection);
            System.out.println(token.getToken());
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        AuthToken comp = aTDao.getAuthToken(token.getToken(), connection);
        assertEquals(token.getToken(), comp.getToken());
        assertEquals(token.getPersonID(), comp.getPersonID());
    }

    @Test
    public void testClearUsers() throws IDao.DatabaseException {
        User user = new User("user", "pass", "email", "Polly", "Grover", 'f', "id");
        User user2 = new User("user2", "pass2", "email", "Polly2", "Grover", 'f', "id2");

        uDao.insertUser(user, connection);
        uDao.insertUser(user2, connection);
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            uDao.clearUsers(connection);
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }

        User comp = uDao.getUser("id", connection);
        User comp2 = uDao.getUser("id2", connection);

        assertEquals(comp, null);
        assertEquals(comp2, null);
    }

    @Test
    public void testExistsUserName(){
        User user = new User("user", "pass", "email", "Polly", "Grover", 'f', "id");
        User user2 = new User("user2", "pass2", "email", "Polly2", "Grover", 'f', "id2");
        try {
            uDao.insertUser(user, connection);
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(uDao.existsUserName(user, connection), true);
        assertEquals(uDao.existsUserName(user2, connection), false);
    }
}
