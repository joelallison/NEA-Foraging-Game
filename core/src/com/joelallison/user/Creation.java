package com.joelallison.user;

import com.badlogic.gdx.graphics.Color;
import com.joelallison.generation.Layer;
import com.joelallison.generation.TerrainLayer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class Creation {
    public String name;
    public ArrayList<Layer> layers;
    Timestamp dateCreated;
    Timestamp lastEdited;
    public Long seed = 0L;

    public Creation(String name, ArrayList<Layer> layers, Timestamp dateCreated, Timestamp lastEdited) {
        this.name = name;
        this.layers = layers;
    }

    public Creation(String name) {
        this.name = name;
        this.layers = new ArrayList<Layer>(){};
        layers.add(new TerrainLayer(seed));
        dateCreated = new Timestamp(System.currentTimeMillis());
        lastEdited = dateCreated;
    }

    // I wondered if I could set creation seed and then call the above constructor...
    // it seems that you can call a constructor from within another constructor,
    // but not in a way that would work here :-(
    public Creation(String name, Long seed) {
        this.name = name;
        this.seed = seed;
        this.layers = new ArrayList<Layer>(){};
        layers.add(new TerrainLayer(seed));
        dateCreated = new Timestamp(System.currentTimeMillis());
        lastEdited = dateCreated;
    }

    public void makeLayerNamesUnique() {
        HashMap<String, Integer> layerCounts = new HashMap<>(this.layers.size());

        for (Layer layer:this.layers) {
            String rawLayerName = layer.getName().replaceAll(" \\([0-9]*\\)$", ""); //strips trailing numbers
            if (!layerCounts.containsKey(rawLayerName)){
                layerCounts.put(rawLayerName, 1); //strips trailing numbers
            } else {
                layerCounts.put(rawLayerName, layerCounts.get(rawLayerName)+1);
            }

            if (layer.getName() == rawLayerName) {layer.setName(rawLayerName + " (" + layerCounts.get(rawLayerName) + ")");}

        }
    }

    public void swapLayers(int moveUp, int moveDown) {
        Layer temp = layers.get(moveUp);
        layers.set(moveUp, layers.get(moveDown));
        layers.set(moveDown, temp);
    }

    public Color getClearColor() {
        return layers.get(0).tileset.getColor();
    }

    public int layerCount() {
        return layers.size();
    }
}