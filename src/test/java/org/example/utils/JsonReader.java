package org.example.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.pojos_bodies.request.UserData;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class JsonReader {

    public static List<UserData> getUsersFromJson(String filePath) throws IOException {
        Gson gson = new Gson();
        FileReader reader = new FileReader(filePath);
        Type userListType = new TypeToken<List<UserData>>(){}.getType();
        List<UserData> users = gson.fromJson(reader, userListType);
        reader.close();
        return users;
    }
}