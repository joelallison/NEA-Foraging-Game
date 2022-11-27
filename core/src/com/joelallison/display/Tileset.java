package com.joelallison.display;

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
    public HashMap<String, Tile> map = new HashMap<String, Tile>(); //Tile name : Tile data

    public Tileset(String name, String spriteSheet, int tileSize) {
        this.name = name;
        this.spriteSheet = spriteSheet;
        this.tileSize = tileSize;
    }

    public Tileset(String name, String spriteSheet, int tileSize, HashMap<String, Tile> map) {
        this.name = name;
        this.spriteSheet = spriteSheet;
        this.tileSize = tileSize;
        this.map = map;
    }

    public static class Tile {
        int cornerX;
        int cornerY;
        public Tile(int cornerX, int cornerY) {
            this.cornerX = cornerX;
            this.cornerY = cornerY;
        }
    }

    public static class TileBound {
        public String name;
        public Float lowerBound;
        public Float upperBound;

        public TileBound(String name, float lowerBound) {
            this.name = name;
            this.lowerBound = lowerBound;
            this.upperBound = -1f;
        }

        public TileBound(String name, float lowerBound, float upperBound) {
            this.name = name;
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        }
    }

    public void initTexture() {
        spriteSheetTexture = new Texture(Gdx.files.internal(this.spriteSheet));
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
