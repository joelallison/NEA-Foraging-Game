package com.joelallison.io;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joelallison.graphics.Tileset;

import java.lang.reflect.Type;
import java.util.HashMap;

public class JsonHandling {
    static Gson gson = new Gson();

    public static HashMap<String, Tileset> tilesetsJsonToMap(String fileLocation) {
        Type type = new TypeToken<HashMap<String, Tileset>>(){}.getType();
        return gson.fromJson(FileHandling.readJSONTileData(fileLocation), type);
    }

}
