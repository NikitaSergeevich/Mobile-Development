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

    private String[] jsonprojectfields = {"author", "description", "title", "link"};

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
        String request = "https://api.parse.com/1/classes/Project/";
        HttpURLConnection connection = request(request, "GET", null);

        final int statusCode = connection.getResponseCode();
        switch (statusCode)
        {
            case HttpURLConnection.HTTP_OK:
                return parseProjects(connection);
            default:
                break;
        }
        return null;
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
                JSONObject obj = array.getJSONObject(i);
                ArrayList<String> jsonparsed = new ArrayList<String>();
                for (String field : jsonprojectfields)
                {
                    if (!obj.isNull(field))
                    {
                        jsonparsed.add(obj.getString(field));
                    }
                    else
                    {
                        jsonparsed.add("");
                    }
                }
                if (!jsonparsed.get(2).equals(""))
                    data.add(new Project(jsonparsed.get(2), jsonparsed.get(1), jsonparsed.get(0), jsonparsed.get(3)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
