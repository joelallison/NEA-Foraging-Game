package com.joelallison.user;

import com.badlogic.gdx.graphics.Color;
import com.joelallison.generation.Layer;
import com.joelallison.generation.TerrainLayer;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static com.joelallison.screens.AppScreen.tilesets;
import static java.time.Instant.now;

public class World {
    public String name;
    public ArrayList<Layer> layers;
    public Instant dateCreated;
    Instant lastAccessed;
    public Long seed = getRandomLong(Long.MAX_VALUE);

    public World(String name, ArrayList<Layer> layers, Long seed, Instant dateCreated) {
        this.name = name;
        this.layers = layers;
        this.seed = seed;
        this.dateCreated = dateCreated;
        //note: lastAccessed just gets written when the world is being saved and sent back to the database.
    }

    public World(String name) {
        this.name = name;
        this.layers = new ArrayList<Layer>(){};
        layers.add(new TerrainLayer(-1L));
        dateCreated = now();
        lastAccessed = dateCreated;
    }
    public World(String name, Long seed) {
        this.name = name;
        this.seed = seed;
        this.layers = new ArrayList<Layer>(){};
        layers.add(new TerrainLayer(seed));
        dateCreated = now();
        lastAccessed = dateCreated;
    }

    public void makeLayerNamesUnique() {
        HashMap<String, Integer> layerCounts = new HashMap<>(this.layers.size());

        for (Layer layer:this.layers) {
            String rawLayerName = layer.getName().replaceAll(" \\([0-9]*\\)$", ""); //strips trailing numbers
            if (!layerCounts.containsKey(rawLayerName)){
                layerCounts.put(rawLayerName, 1); //stores layer name without any numbers at the end
            } else {
                layerCounts.put(rawLayerName, layerCounts.get(rawLayerName)+1);
            }

            if (layer.getName() == rawLayerName) {layer.setName(rawLayerName + " (" + layerCounts.get(rawLayerName) + ")");}

        }
    }

    public Long getRandomLong(Long max) {
        return (new Random()).nextLong(max);
    }

    public void swapLayers(int layerOne, int layerTwo) {
        //used to move a layer up or down
        //(and then move the layer in the space it moves into into its old space)

        Layer temp = layers.get(layerOne);
        layers.set(layerOne, layers.get(layerTwo));
        layers.set(layerTwo, temp);
    }

    public Long getLayerSeed(int i) {
        if (layers.get(i).inheritSeed()){
            return seed;
        } else {
            return layers.get(i).getSeed();
        }
    }

    public Color getClearColor() {
        return tilesets.get(layers.get(0).tilesetName).getColor();
    }

    public int layerCount() {
        return layers.size();
    }
}