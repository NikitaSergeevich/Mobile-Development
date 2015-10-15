package com.innopolis.example.assignment1.Utils;

import com.innopolis.example.assignment1.Dto.UserDto;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mg_000 on 07.10.2015.
 */
public class JSONParser {
    public static UserDto parseUser(String userJsonString){
        try {
            JSONObject userJson = new JSONObject(userJsonString);

            UserDto userDto = new UserDto();
            if (userJson.has("username"))
                userDto.Name = userJson.getString("username");
            if (userJson.has("objectId"))
                userDto.UserId = userJson.getString("objectId");
            //todo parse create and update dates, currently they are ignored
            return userDto;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String serializeUser(UserDto userDto){
        try {
            JSONObject userJson = new JSONObject();
            userJson.put("username", userDto.Name);
            return userJson.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
