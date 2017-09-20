package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.Connection;
import java.util.Vector;

import access.Access;
import access.AuthTokenDao;
import model.AuthToken;
import model.Person;
import result.ErrorMessage;
import service.EventService;
import service.PersonService;

/**
 * Created by Andrew1 on 5/23/17.
 */

public class PersonHandler extends BaseHandler implements HttpHandler{
    private int expire;

    public PersonHandler(int expire) {
        this.expire = expire;
    }

    public void handle(HttpExchange exchange) throws IOException {
        deleteExpiredAuthTokens(expire);

        PersonService service = new PersonService();
        AuthTokenDao aTDao = new AuthTokenDao();

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {

                Headers reqHeaders = exchange.getRequestHeaders();
                String result = null;
                Gson g = new Gson();

                Access access = new Access();
                Connection connection = access.connect();
                boolean allDone = true;

                if (reqHeaders.containsKey("Authorization")) {
                    String authToken = reqHeaders.getFirst("Authorization");
                    AuthToken token = aTDao.getAuthToken(authToken, connection);
                    if (token == null) {
                        allDone = false;
                        access.commitRoll(allDone);
                        ErrorMessage message = new ErrorMessage("AuthToken does not exist");
                        result = g.toJson(message);
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    } else {
                        access.commitRoll(allDone);
                        Vector<Person> people = service.allPeople(token);
                        result = g.toJson(people);
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    }

                    OutputStream respBody = exchange.getResponseBody();
                    writeString(result, respBody);
                    respBody.close();
                }
            }
        }
        catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
