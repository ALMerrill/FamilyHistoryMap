package handler;
import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;
import service.LoginService;
import request.LoginRequest;
import result.LoginResult;
import com.google.gson.Gson;

public class LoginHandler extends BaseHandler implements HttpHandler{


    @Override
    public void handle(HttpExchange exchange) throws IOException {
        LoginService service = new LoginService();

        boolean success = false;
        try {

            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                InputStream reqBody = exchange.getRequestBody();
                String reqData = readString(reqBody);

                Gson g = new Gson();
                LoginRequest request = g.fromJson(reqData, LoginRequest.class);
                LoginResult result = service.login(request);
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
