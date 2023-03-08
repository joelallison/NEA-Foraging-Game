package com.joelallison.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.joelallison.screens.AppScreen;

import java.util.HashMap;

public class Tileset {
    public String creator;
    private String spriteSheet;
    private Texture spriteSheetTexture;
    public int tileSize;
    private Color baseColor;
    private String baseColorHex;
    public String defaultTile;
    public HashMap<String, TileCorners> map = new HashMap<String, TileCorners>(); //Tile name : Tile location

    public Tileset(String creator, String spriteSheet, int tileSize, String baseColorHex, String defaultTile, HashMap<String, TileCorners> map) {
        this.creator = creator;
        this.spriteSheet = spriteSheet;
        this.tileSize = tileSize;
        this.map = map;
        this.baseColorHex = baseColorHex;
        this.defaultTile = defaultTile;
    }

    public static class TileRef {
        public String tilesetName;
        public String tileName;

        public TileRef(String tileset, String tile) {
            this.tilesetName = tileset;
            this.tileName = tile;
        }

    }


    public static class TileCorners {
        //basically identical to IntPair from AppScreen, but the different name improves legibility.
        int cornerX;
        int cornerY;
        public TileCorners(int cornerX, int cornerY) {
            this.cornerX = cornerX;
            this.cornerY = cornerY;
        }
    }

    public static abstract class TileSpec {
        public String name; //universal across all

        public TileSpec(String name) { //for TerrainLayer
            this.name = name;
        }


    }

    //for Terrain
    public static class TerrainTileSpec extends TileSpec {
        public Float lowerBound;
        public TerrainTileSpec(String name, Float lowerBound) {
            super(name);
            this.lowerBound = lowerBound;
        }
        public TerrainTileSpec(String name) {
            super(name);
            this.lowerBound = 0.5f;
        }
    }

    //for Maze
    public static class MazeTileSpec extends TileSpec {
        public int orientationID;
        public MazeTileSpec(String name, int orientationID) {
            super(name);
            this.orientationID = orientationID;
        }

        public MazeTileSpec(String name) {
            super(name);
            this.orientationID = 1;
        }
    }

    public void initTileset(String spritesheetLocation) {
        spriteSheetTexture = new Texture(spritesheetLocation);
        baseColor = Color.valueOf(this.baseColorHex);
    }

    public Color getColor() {
        return baseColor;
    }

    public void setColor(Color color) {
        this.baseColor = color;
    }

    public TextureRegion getTileTextureFromName(String name) {
        if (!name.equals("-")) {
            TileCorners corners = this.map.get(name);
            return new TextureRegion(this.spriteSheetTexture, getActualTileLocation(corners.cornerX), getActualTileLocation(corners.cornerY), tileSize, tileSize);
        }

        else { return null; }
    }

    public int getActualTileLocation(int location) { //input will be cornerX or cornerY, as those values are relative and not actual pixel values
        return tileSize * location;
    }

    public String getSpriteSheetName() {
        return spriteSheet;
    }

    public void setSpriteSheetName(String spriteSheet) {
        this.spriteSheet = spriteSheet;
    }

    public Texture getSpriteSheet() {
        return spriteSheetTexture;
    }

    public void setSpriteSheet(String fileLocation) { // used specifically where using with Gdx.files.internal isn't the right option
        spriteSheetTexture = new Texture(fileLocation);
    }



}
