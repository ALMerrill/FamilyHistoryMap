package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.sql.Connection;

import access.Access;
import request.LoadRequest;
import request.LoginRequest;
import result.ClearResult;
import result.LoadResult;
import service.ClearService;
import service.LoadService;

/**
 * Created by Andrew1 on 5/23/17.
 */

public class LoadHandler extends BaseHandler implements HttpHandler{
    public LoadHandler(){}

    public void handle(HttpExchange exchange) throws IOException {
        LoadService loader = new LoadService();
        ClearService clearer = new ClearService();

        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                Access access = new Access();
                Connection connection = access.connect();
                boolean allDone = true;

                Gson g = new Gson();
                ClearResult cResult = clearer.clear(connection);
                if(cResult.getMessage() == "Unable to clear data")
                    allDone = false;

                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);
                LoadRequest request = g.fromJson(reqData, LoadRequest.class);
                LoadResult lResult = null;
                if(request.getEvents().length == 0 && request.getPersons().length == 0 && request.getUsers().length == 0)
                    lResult = new LoadResult("No data received");
                else
                    lResult = loader.load(request, connection);
                if(lResult.getMessage() == "Failed to load the data")
                    allDone = false;

                access.commitRoll(allDone);
                String JsonResult = g.toJson(lResult);
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
