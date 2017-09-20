package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.Connection;

import access.Access;
import access.AuthTokenDao;
import model.AuthToken;
import model.Event;
import result.ErrorMessage;
import service.EventService;

/**
 * Created by Andrew1 on 5/30/17.
 */

public class EventIDHandler extends BaseHandler implements HttpHandler {
    private int expire;

    public EventIDHandler(int expire) {
        this.expire = expire;
    }

    public void handle(HttpExchange exchange) throws IOException {
        deleteExpiredAuthTokens(expire);

        EventService service = new EventService();
        AuthTokenDao aTDao = new AuthTokenDao();

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {

                Headers reqHeaders = exchange.getRequestHeaders();

                if (reqHeaders.containsKey("Authorization")) {
                    String authToken = reqHeaders.getFirst("Authorization");
                    String eventID = exchange.getRequestURI().getPath().substring(7);
                    System.out.println(eventID);

                    Event event = service.event(eventID);

                    Access access = new Access();
                    Connection connection = access.connect();
                    boolean allDone = true;
                    AuthToken token = aTDao.getAuthToken(authToken, connection);

                    String result = null;
                    Gson g = new Gson();

                    if(token != null && event != null) {
                        String userName = token.getUserName();
                        String descendant = event.getDescendant();

                        if (userName.equals(descendant)) {
                            result = g.toJson(event);
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        }
                        else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                            ErrorMessage message = new ErrorMessage("Invalid AuthToken");
                            result = g.toJson(message);
                        }
                    }
                    else if(token == null){
                        allDone = false;
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        ErrorMessage message = new ErrorMessage("AuthToken does not exist");
                        result = g.toJson(message);
                    }
                    else if(event == null){
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        ErrorMessage message = new ErrorMessage("Event ID does not exist");
                        result = g.toJson(message);
                    }
                    access.commitRoll(allDone);
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
