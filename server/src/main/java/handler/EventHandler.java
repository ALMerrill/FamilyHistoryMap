package handler;
import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;
import com.google.gson.Gson;

import access.Access;
import access.AuthTokenDao;
import model.AuthToken;
import result.ErrorMessage;
import service.EventService;
import model.Event;

import java.io.IOException;
import java.sql.Connection;
import java.util.Vector;

import static com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler.BUFFER_SIZE;

/**
 * Created by Andrew1 on 5/23/17.
 */

public class EventHandler extends BaseHandler implements HttpHandler {
    private int expire;

    public EventHandler(int expire) {
        this.expire = expire;
    }


    public void handle(HttpExchange exchange) throws IOException {
        deleteExpiredAuthTokens(expire);

        EventService service = new EventService();
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
                        System.out.println("AuthToken does not exist");
                        result = g.toJson(message);
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        OutputStream respBody = exchange.getResponseBody();
                        writeString(result, respBody);
                        respBody.close();
                    } else {
                        access.commitRoll(allDone);
                        Vector<Event> events = service.allEvents(token);
                        result = g.toJson(events);
                        System.out.println("OK");
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        OutputStream respBody = exchange.getResponseBody();
                        writeString(result, respBody);
                        respBody.close();
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("EventHandler IOException");
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}


