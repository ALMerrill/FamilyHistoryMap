package handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.nio.file.*;
import java.io.IOException;

/**
 * Created by Andrew1 on 5/23/17.
 */

public class DefaultHandler extends BaseHandler implements HttpHandler{
    public DefaultHandler(){}

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                String uri = exchange.getRequestURI().getPath();
                String filePathStr = "/Users/Andrew1/Documents/Spring2017/AndroidStudioProjects/FamilyMapServer/server/web" + exchange.getRequestURI();
                if(uri.equals("/")){
                    filePathStr += "index.html";
                }
                Path filePath = FileSystems.getDefault().getPath(filePathStr);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                Files.copy(filePath, exchange.getResponseBody());
                exchange.getResponseBody().close();
                success = true;
            }
            if(!success){
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }
        }
        catch (IOException e){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }





    }
}
