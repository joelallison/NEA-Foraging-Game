package com.joelallison.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

public class Tileset {
    private String name;
    private String spriteSheet;
    private Texture spriteSheetTexture;
    public int tileSize;
    private Color baseColor;
    private String baseColorHex;
    public HashMap<String, Tile> map = new HashMap<String, Tile>(); //Tile name : Tile data

    public Tileset(String name, String spriteSheet, int tileSize) {
        this.name = name;
        this.spriteSheet = spriteSheet;
        this.tileSize = tileSize;
    }

    public Tileset(String name, String spriteSheet, int tileSize, String baseColorHex, HashMap<String, Tile> map) {
        this.name = name;
        this.spriteSheet = spriteSheet;
        this.tileSize = tileSize;
        this.map = map;
        this.baseColorHex = baseColorHex;
    }

    public static class Tile {
        int cornerX;
        int cornerY;
        public Tile(int cornerX, int cornerY) {
            this.cornerX = cornerX;
            this.cornerY = cornerY;
        }
    }

    public static class TileChild {
        public String name; //universal
        public Float lowerBound; //for TerrainLayer TileChildren

        public String orientationID; //for MazeLayer TileChildren

        public TileChild(String name, float lowerBound) { //for TerrainLayer
            this.name = name;
            this.lowerBound = lowerBound;
        }


    }

    public void initTileset() {
        spriteSheetTexture = new Texture(Gdx.files.internal(this.spriteSheet));
        baseColor = Color.valueOf(this.baseColorHex);
    }

    public Color getColor() {
        return baseColor;
    }

    public void setColor(Color color) {
        this.baseColor = color;
    }

    public TextureRegion getTileTexture(Tile tile) {
        return new TextureRegion(this.spriteSheetTexture, getActualTileLocation(tile.cornerX), getActualTileLocation(tile.cornerY), tileSize, tileSize);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
