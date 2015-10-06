package com.innopolis.example.assignment1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ParseServerAuthenticate implements ServerAuthenticate{
    @Override
    public String userSignUp(String name, String email, String pass, String authType) throws Exception {
        URL requestURL	= new URL("https://api.parse.com/1/classes/_User/");
        HttpURLConnection connection = (HttpURLConnection)requestURL.openConnection();
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Parse-REST-API-Key", "xjsAJgz5l6I1Le5FHXJj1tJyb6U7oIbbVzO25WWW");
        connection.setRequestProperty("X-Parse-Application-Id", "rjCPaMFw1wQZRVOc78k5AJhU38322get8FUsOqv3");

        connection.setDoInput(true);
        connection.connect();
        final int statusCode = connection.getResponseCode();

        switch (statusCode)
        {
            case HttpURLConnection.HTTP_UNAUTHORIZED:
                readBuffer(connection);
                break;
            default:
                break;
        }
        String authtoken = null;
        return authtoken;
    }

    @Override
    public String userSignIn(String user, String pass, String authType) throws Exception {
        String response = "";

        URL requestURL	= new URL("https://api.parse.com/1/classes/_User/");
        HttpURLConnection connection = (HttpURLConnection)requestURL.openConnection();
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Parse-REST-API-Key", "xjsAJgz5l6I1Le5FHXJj1tJyb6U7oIbbVzO25WWW");
        connection.setRequestProperty("X-Parse-Application-Id", "rjCPaMFw1wQZRVOc78k5AJhU38322get8FUsOqv3");
        connection.setDoInput(true);
        connection.connect();
        final int statusCode = connection.getResponseCode();

        switch (statusCode)
        {
            case HttpURLConnection.HTTP_OK:
                response = readBuffer(connection);
                if (parseBuffer(response, user))
                    return "OK";
                break;
            default:
                break;
        }

        return "NOTOK";
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

    private boolean parseBuffer(String response, String username)
    {
        JSONObject json	= null;
        ArrayList<String> data = new ArrayList<String>();

        try {
            json = new JSONObject(response);
            JSONArray array	= json.getJSONArray("results");
            for (int i = 0; i < array.length(); i++)
            {
                if (username.equals(array.getJSONObject(i).getString("username")))
                    return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
