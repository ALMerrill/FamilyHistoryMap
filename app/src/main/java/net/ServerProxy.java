package net;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import model.Event;
import model.Person;
import model.User;
import request.LoginRequest;
import request.RegisterRequest;
import result.ErrorMessage;
import result.LoginResult;
import result.RegisterResult;

/**
 * Created by Andrew1 on 6/3/17.
 */

public class ServerProxy {
    private String host;
    private int port;

    public ServerProxy(String host, int port){
        this.host = host;
        this.port = port;
    }

    public LoginResult login(LoginRequest request) throws MalformedURLException{    //url will be http://host#:port# to connect to server
        try {
            URL url = new URL("http://" + host + ":" + port + "/user/login");

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("POST");
            http.setDoOutput(true);

            http.connect();
            Gson gson = new Gson();
            String reqData = gson.toJson(request);

            OutputStream reqBody = http.getOutputStream();

            writeString(reqData, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Login Succeeded");
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                System.out.println(respData);
                return gson.fromJson(respData, LoginResult.class);
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
        }
        catch (IOException e) {
            System.out.println("Login IOException");
            e.printStackTrace();
        }
        System.out.println("Error on login");
        return null;
    }

    public RegisterResult register(RegisterRequest request) throws MalformedURLException{
        try {
            URL url = new URL("http://" + host + ":" + port + "/user/register");

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.connect();
            Gson gson = new Gson();
            String reqData = gson.toJson(request);

            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("Register Succeeded");
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                System.out.println(respData);
                return gson.fromJson(respData, RegisterResult.class);
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
        }
        catch (IOException e) {
            System.out.println("Register IOException");
            e.printStackTrace();
        }
        System.out.println("Error on register");
        return null;
    }

    public Object[] getAllData(String authToken, String handle) throws MalformedURLException{
        try {
            URL url = new URL("http://" + host + ":" + port + "/" + handle);

            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("GET");
            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Accept", "application/json");

            http.connect();



            int responseCode = 0;
            Gson gson = new Gson();
            try{
                responseCode = http.getResponseCode();
            } catch(Exception e) {
                System.out.println("Response code exception");
            }


            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Event retrieval succeeded");
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                System.out.println(respData);
                Object[] result = null;
                if(handle.equals("event"))
                    result = gson.fromJson(respData, Event[].class);
                else if(handle.equals("person"))
                    result = gson.fromJson(respData, Person[].class);
                return result;
            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());
            }
        }
        catch (IOException e) {
            System.out.println("Event IOException");
            e.printStackTrace();
        }
        System.out.println("Error on event retrieval");
        return null;
    }

    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
