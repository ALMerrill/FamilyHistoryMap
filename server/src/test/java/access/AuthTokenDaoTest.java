package access;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

import model.AuthToken;
import service.ClearService;


/**
 * Created by Andrew1 on 5/25/17.
 */

public class AuthTokenDaoTest {
    private Access access;
    private Connection connection;
    private AuthTokenDao aTDao;

    public AuthTokenDaoTest(){}

    @Before
    public void setUp(){
        access = new Access();
        connection = access.connect();
        aTDao = new AuthTokenDao();
    }

    @After
    public void tearDown(){
        ClearService service = new ClearService();
        service.clear(connection);
        access.commitRoll(true);
    }

    @Test
    public void testInsertAndGetAuthToken() {
        AuthToken token = new AuthToken("lkfnqonaakfni", "1", "user", 234098);

        try {
            aTDao.insertAuthToken(token, connection);
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        AuthToken compare = aTDao.getAuthToken("lkfnqonaakfni", connection);

        assertEquals(token.getToken(), compare.getToken());
        assertEquals(token.getPersonID(), compare.getPersonID());
        assertEquals(token.getUserName(), compare.getUserName());
        assertEquals(token.getTimeStamp(), compare.getTimeStamp());

    }

    @Test
    public void testClearExpiredAuthTokens() {
        int expire = 0; //0 time to expire, all tokens will be expired
        AuthToken token = new AuthToken("123456789", "qwertyuiop", "username", 100);
        try {
            aTDao.insertAuthToken(token, connection);
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            aTDao.clearExpiredAuthTokens(expire, connection);
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }
        AuthToken shouldBeNull = aTDao.getAuthToken("123456789", connection);

        assertEquals(shouldBeNull, null);
    }

    @Test
    public void testClearAuthTokens() {
        AuthToken token = new AuthToken("123456789", "qwertyuiop", "username", 100);
        AuthToken token2 = new AuthToken("987654321", "poiuytrewq", "username2", 100);
        try {
            aTDao.insertAuthToken(token, connection);
            aTDao.insertAuthToken(token2, connection);
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            aTDao.clearAuthTokens(connection);
        } catch (IDao.DatabaseException e) {
            e.printStackTrace();
        }
        AuthToken shouldBeNull = aTDao.getAuthToken("123456789", connection);
        AuthToken shouldBeNull2 = aTDao.getAuthToken("987654321", connection);


        assertEquals(shouldBeNull, null);
    }
}
