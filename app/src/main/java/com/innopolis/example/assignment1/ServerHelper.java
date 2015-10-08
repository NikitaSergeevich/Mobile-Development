package com.innopolis.example.assignment1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ServerHelper{

    public boolean userSignUp(String user, String email) throws Exception {
        JSONObject jsonParam = new JSONObject();
        JSONArray array = null;
        jsonParam.put("username", user);
        jsonParam.put("password", email);
        String request = "https://api.parse.com/1/classes/_User";
        HttpURLConnection connection = request(request, "POST", jsonParam);

        final int statusCode = connection.getResponseCode();
        switch (statusCode)
        {
            case HttpURLConnection.HTTP_OK:
                array = parseBuffer(connection);
                if(array.length() == 0)
                    return true;
            default:
                break;
        }
        return false;
    }

    public boolean userSignIn(String user, String pass) throws Exception {
        String request = "https://api.parse.com/1/classes/_User?where={\"username\":\"" + user + "\"}";
        HttpURLConnection connection = request(request, "GET", null);
        JSONArray array = null;

        final int statusCode = connection.getResponseCode();
        switch (statusCode)
        {
            case HttpURLConnection.HTTP_OK:
                array = parseBuffer(connection);
                if(array.length() != 0)
                    return true;
            default:
                break;
        }
        return false;
    }

    public ArrayList<Project> getProjects() throws Exception {
        String request = "https://api.parse.com/1/classes/Projects";
        HttpURLConnection connection = request(request, "GET", null);
        return parseProjects(connection);
    }

    public HttpURLConnection request(String request, String method, JSONObject params) throws Exception
    {
        URL requestURL	= new URL(request);
        HttpURLConnection connection = (HttpURLConnection)requestURL.openConnection();
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.setRequestMethod(method);
        connection.setRequestProperty("X-Parse-REST-API-Key", "xjsAJgz5l6I1Le5FHXJj1tJyb6U7oIbbVzO25WWW");
        connection.setRequestProperty("X-Parse-Application-Id", "rjCPaMFw1wQZRVOc78k5AJhU38322get8FUsOqv3");
        connection.setDoInput(true);
        if (method.equals("DELETE") | method.equals("POST") | method.equals("PUT"))
        {
            DataOutputStream printout = new DataOutputStream(connection.getOutputStream());
            printout.write(params.toString().getBytes());
            printout.close();
        }
        connection.connect();
        return connection;
    }

    private String readBuffer(HttpURLConnection connection) throws IOException {
        BufferedReader reader =	new	BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line	= reader.readLine();
        while (line	!=	null)	{
            sb.append(line + "\n");
            line = reader.readLine();
        }
        return sb.toString();
    }

    private JSONArray parseBuffer(HttpURLConnection connection) throws IOException {
        JSONObject json	= null;
        JSONArray array = null;
        String response = readBuffer(connection);

        try {
            json = new JSONObject(response);
            array = json.getJSONArray("results");
            return array;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return array;
    }

    private ArrayList<Project> parseProjects(HttpURLConnection connection) throws IOException {
        JSONObject json	= null;
        ArrayList<Project> data = new ArrayList<Project>();
        String response = readBuffer(connection);

        try {
            json = new JSONObject(response);
            JSONArray array	= json.getJSONArray("results");
            for (int i = 0; i < array.length(); i++)
            {
                String author = array.getJSONObject(i).getString("author");
                String description = array.getJSONObject(i).getString("description");
                String name = array.getJSONObject(i).getString("title");
                String link = array.getJSONObject(i).getString("link");
                String objectId = array.getJSONObject(i).getString("objectId");
                data.add(new Project(name, description, author, link));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
