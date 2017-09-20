package server;
import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;
import handler.*;

/**
 * Created by Andrew1 on 5/23/17.
 */

public class Server {
    private static final int MAX_WAITING_CONNECTIONS = 12;
    private HttpServer server;

    private void run(String portNumber, int expire) {

        System.out.println("Initializing HTTP Server");

        try {
            server = HttpServer.create(
                    new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        server.setExecutor(null);

        System.out.println("Creating contexts");

        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/fill", new FillHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/person/", new PersonIDHandler(expire));
        server.createContext("/person", new PersonHandler(expire));
        server.createContext("/event/", new EventIDHandler(expire));
        server.createContext("/event", new EventHandler(expire));
        server.createContext("/", new DefaultHandler());


        System.out.println("Starting server");


        server.start();

        System.out.println("Server started");
    }



    public static void main(String args[]){
        String portNumber = args[0];
        int expire = Integer.parseInt(args[1]);
        new Server().run(portNumber, expire);
    }
}
