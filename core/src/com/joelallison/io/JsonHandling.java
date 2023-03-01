package com.joelallison.io;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joelallison.graphics.Tileset;

import java.io.File;
import java.lang.reflect.Type;

public abstract class JsonHandling {
    static Gson gson = new Gson();
    public static Tileset tilesetJsonToObject(String jsonString) {
        Type type = new TypeToken<Tileset>(){}.getType();
        return gson.fromJson(jsonString, type);
    }

}
