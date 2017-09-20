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
import model.Person;
import request.RegisterRequest;
import result.RegisterResult;
import service.FillService;
import service.RegisterService;

/**
 * Created by Andrew1 on 5/23/17.
 */

public class RegisterHandler extends BaseHandler implements HttpHandler{
    public RegisterHandler(){}

    public void handle(HttpExchange exchange) throws IOException {
        RegisterService service = new RegisterService();
        FillService fService = new FillService();
        FillHandler filler = new FillHandler();

        boolean success = false;
        try {

            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);

                Gson g = new Gson();
                RegisterRequest request = g.fromJson(reqData, RegisterRequest.class);
                RegisterResult result = service.register(request);
                if(result.getUsername() != null) {
                    Person creation = service.getPerson();
                    Access access = new Access();
                    Connection connection = access.connect();
                    boolean allDone = true;
                    if(!fService.createAllGenerations(creation, 4, connection))
                        allDone = false;
                    access.commitRoll(allDone);
                }
                String JsonResult = g.toJson(result);

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
