package com.joelallison.io;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.joelallison.graphics.Tileset;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public abstract class JsonHandling {
    static Gson gson = new Gson();
    public static FileHandling.TilesetEntry<String, Tileset> tilesetJsonToMapEntry(File jsonFile) {
        Type type = new TypeToken<FileHandling.TilesetEntry<String, Tileset>>(){}.getType();
        return gson.fromJson(FileHandling.jsonFileToString(jsonFile), type);
    }

}
