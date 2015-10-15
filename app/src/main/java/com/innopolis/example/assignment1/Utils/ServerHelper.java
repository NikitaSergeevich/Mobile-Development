package com.innopolis.example.assignment1.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.innopolis.example.assignment1.Project;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ServerHelper{

    private final static String[] jsonprojectfields = {"author", "description", "title", "link"};

    public static ArrayList<Project> getProjects() throws Exception {
        String request = "https://api.parse.com/1/classes/Project/";
        HttpURLConnection connection = performConnection(request, "GET", null);

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

    private static HttpURLConnection performConnection(String url, String requestMethod, JSONObject params) throws IOException {
        URL requestUrl = new URL(url); //userId used as a password
        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.setRequestMethod(requestMethod);
        connection.setRequestProperty("X-Parse-REST-API-Key", "xjsAJgz5l6I1Le5FHXJj1tJyb6U7oIbbVzO25WWW");
        connection.setRequestProperty("X-Parse-Application-Id", "rjCPaMFw1wQZRVOc78k5AJhU38322get8FUsOqv3");
        connection.setDoInput(true);

        if (requestMethod == "POST") {
            connection.setDoOutput(true);
        }

        if (params != null && (requestMethod.equals("DELETE") | requestMethod.equals("POST") | requestMethod.equals("PUT")))
        {
            DataOutputStream printout = new DataOutputStream(connection.getOutputStream());
            printout.write(params.toString().getBytes());
            printout.close();
        }

        connection.connect();
        return connection;
    }


    private static String readBuffer(InputStream stream) throws IOException {
        BufferedReader reader =	new	BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        String line	= reader.readLine();
        while (line	!=	null)	{
            sb.append(line + "\n");
            line = reader.readLine();
        }
        stream.close();
        return sb.toString();
    }

    private static ArrayList<Project> parseProjects(HttpURLConnection connection) throws IOException {
        JSONObject json	= null;
        ArrayList<Project> data = new ArrayList<Project>();
        String response = readBuffer(connection.getInputStream());

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

    public static String makeGETRequest(String url) {
        InputStream is = null;
        try {
            HttpURLConnection connection = performConnection(url, "GET", null);
            //todo should scheck response code like this: int responseCode = connection.getResponseCode();
            is = connection.getInputStream();
            return readBuffer(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String makePOSTRequest(String url, String data) {
        InputStream is = null;
        try {
            HttpURLConnection connection = performConnection(url, "POST", null);
            try (OutputStream output = connection.getOutputStream()) {
                output.write(data.getBytes());
            }
            is = connection.getInputStream();
            return readBuffer(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean connectionIsAvailable(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
