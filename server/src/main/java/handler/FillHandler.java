package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import model.FirstNames;
import model.LastNames;
import model.Locations;
import request.FillRequest;
import result.FillResult;
import service.FillService;

/**
 * Created by Andrew1 on 5/23/17.
 */

public class FillHandler extends BaseHandler implements HttpHandler{
    public FillHandler(){}
    private FirstNames fnames;
    private FirstNames mnames;
    private LastNames snames;
    private Locations locations;
    private String[] eventTypes = new String[] {"birth", "baptism", "christening", "marriage", "death"};

    public void handle(HttpExchange exchange) throws IOException {
        FillService service = new FillService();

        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                String req = exchange.getRequestURI().toString();
                String[] uri = req.split("/");
                String username = uri[2];
                String generations = "4";       //if generations is not in the URI, it will be set to the default of 4.
                if(uri.length == 4)             //otherwise set to the given generations parameter
                    generations = uri[3];

                Gson g = new Gson();

                FillRequest request = new FillRequest(username, Integer.parseInt(generations));
                FillResult result = service.fill(request);
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

    public void initializeData(){
        Gson gson = new Gson();
        FileReader reader = null;
        try {
            reader = new FileReader("/Users/Andrew1/Documents/Spring2017/AndroidStudioProjects/FamilyMapServer/server/libs/familymapserver/data/json/fnames.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        fnames = gson.fromJson(reader, FirstNames.class);

        try {
            reader = new FileReader("/Users/Andrew1/Documents/Spring2017/AndroidStudioProjects/FamilyMapServer/server/libs/familymapserver/data/json/mnames.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mnames = gson.fromJson(reader, FirstNames.class);

        try {
            reader = new FileReader("/Users/Andrew1/Documents/Spring2017/AndroidStudioProjects/FamilyMapServer/server/libs/familymapserver/data/json/snames.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        snames = gson.fromJson(reader, LastNames.class);

        try {
            reader = new FileReader("/Users/Andrew1/Documents/Spring2017/AndroidStudioProjects/FamilyMapServer/server/libs/familymapserver/data/json/locations.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        locations = gson.fromJson(reader, Locations.class);
    }

    public FirstNames getFnames() {
        return fnames;
    }

    public void setFnames(FirstNames fnames) {
        this.fnames = fnames;
    }

    public FirstNames getMnames() {
        return mnames;
    }

    public void setMnames(FirstNames mnames) {
        this.mnames = mnames;
    }

    public LastNames getSnames() {
        return snames;
    }

    public void setSnames(LastNames snames) {
        this.snames = snames;
    }

    public Locations getLocations() {
        return locations;
    }

    public void setLocations(Locations locations) {
        this.locations = locations;
    }

    public String[] getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(String[] eventTypes) {
        this.eventTypes = eventTypes;
    }
}
