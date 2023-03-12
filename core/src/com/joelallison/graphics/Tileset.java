package com.joelallison.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

public class Tileset {
    private String creator;
    private String spriteSheet;
    private Texture spritesheetTexture;
    private int tileSize;
    private Color baseColor;
    private String baseColorHex;
    public HashMap<String, TileCorner> map = new HashMap<String, TileCorner>(); //Tile name : Tile location

    public Tileset(String creator, String spriteSheet, int tileSize, String baseColorHex, String defaultTile, HashMap<String, TileCorner> map) {
        this.creator = creator;
        this.spriteSheet = spriteSheet;
        this.tileSize = tileSize;
        this.map = map;
        this.baseColorHex = baseColorHex;
    }


    public static class TileCorner {
        int cornerX;
        int cornerY;
        public TileCorner(int cornerX, int cornerY) {
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
        public boolean[][] neighbourMap;
        public MazeTileSpec(String name, boolean[][] neighbourMap) {
            super(name);
            this.neighbourMap = neighbourMap;
        }

        public MazeTileSpec(String name) {
            super(name);
            //this is the neighbourMap for a tile with borders on all sides.
            this.neighbourMap = new boolean[][] {
                    {false, false, false},
                    {false, true, false},
                    {false, false, false}
            };
        }

        public static boolean[][] neighbourMapParseString(String neighbourMapString) {
            boolean[][] oMap = new boolean[3][3];
            for (int i = 0; i < neighbourMapString.length(); i++) {
                oMap[i / 3][i % 3] = !(Character.getNumericValue(neighbourMapString.charAt(i)) == 0); //converts char to int to boolean
            }

            return oMap;
        }

        public String neighbourMapToString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (neighbourMap[i][j]) {
                        sb.append("1");
                    }else {
                        sb.append("0");
                    }
                }
            }
            return sb.toString();
        }
    }

    public void initTileset(String spritesheetLocation) {
        spritesheetTexture = new Texture(spritesheetLocation);
        baseColor = Color.valueOf(this.baseColorHex);
    }

    public TextureRegion getTileTextureFromName(String name) {
        if (!name.equals("-")) {
            TileCorner corners = this.map.get(name);
            return new TextureRegion(this.spritesheetTexture, calcActualTileCoord(corners.cornerX), calcActualTileCoord(corners.cornerY), tileSize, tileSize);
        }

        else { return null; }
    }
    private int calcActualTileCoord(int location) { //input will be cornerX or cornerY, as those values are relative and not actual pixel values
        return tileSize * location;
    }
    public String getCreator() {
        return creator;
    }
    public Color getColor() {
        return baseColor;
    }
    public String getSpritesheetName() {
        return spriteSheet;
    }
    public Texture getSpritesheetTexture() {
        return spritesheetTexture;
    }
}
