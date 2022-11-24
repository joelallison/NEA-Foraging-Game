package com.joelallison.display;

import com.google.gson.Gson;

public class JsonToObject {

    public static Tileset getTilesetObject(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Tileset.class);
    }



}
