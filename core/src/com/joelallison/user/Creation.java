package com.joelallison.user;

import com.badlogic.gdx.graphics.Color;
import com.joelallison.generation.Layer;
import com.joelallison.generation.TerrainLayer;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Creation {
    public String name;
    public static ArrayList<Layer> layers;
    Timestamp dateCreated;
    Timestamp lastEdited;
    Long creationSeed = 0L;

    public Creation(String name, ArrayList<Layer> layers, Timestamp dateCreated, Timestamp lastEdited) {
        this.name = name;
        this.layers = layers;
    }

    public Creation(String name) {
        this.name = name;
        this.layers = new ArrayList<Layer>(){};
        layers.add(new TerrainLayer(creationSeed));
        dateCreated = new Timestamp(System.currentTimeMillis());
        lastEdited = dateCreated;
    }

    // I wondered if I could set creation seed and then call the above constructor...
    // it seems that you can call a constructor from within another constructor,
    // but not in a way that would work here :-(
    public Creation(String name, Long seed) {
        this.name = name;
        this.creationSeed = seed;
        this.layers = new ArrayList<Layer>(){};
        layers.add(new TerrainLayer(creationSeed));
        dateCreated = new Timestamp(System.currentTimeMillis());
        lastEdited = dateCreated;
    }

    public void makeLayerNamesUnique() {
        //this.layers something something
    }

    public Color getClearColor() {
        return layers.get(0).tileset.getColor();
    }

    public int layerCount() {
        return layers.size();
    }
}