package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.Connection;

import access.Access;
import result.ClearResult;
import service.ClearService;

/**
 * Created by Andrew1 on 5/23/17.
 */

public class ClearHandler extends BaseHandler implements HttpHandler {
    public ClearHandler(){}

    public void handle(HttpExchange exchange) throws IOException {
        ClearService service = new ClearService();

        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                Access access = new Access();
                Connection connection = access.connect();
                boolean allDone = true;

                Gson g = new Gson();
                ClearResult result = service.clear(connection);
                if(result.getMessage() == "Unable to clear data")
                    allDone = false;
                access.commitRoll(allDone);

                String JsonResult = g.toJson(result);

                System.out.println(JsonResult);

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream respBody = exchange.getResponseBody();
                writeString(JsonResult, respBody);
                respBody.close();

                success = true;
            }

            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

                exchange.getResponseBody().close();
            }
        }
        catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }

}
