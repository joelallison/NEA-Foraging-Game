package com.joelallison.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.joelallison.generation.FileHandling;

import java.util.HashMap;




import static com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpawnShape.line;

public class Tileset {
    private String name;
    private String spriteSheet;
    public int tileSize;
    public HashMap<String, Tile> map = new HashMap<String, Tile>();

    public Tileset(String name, String spriteSheet, int tileSize) {
        this.name = name;
        this.spriteSheet = spriteSheet;
        this.tileSize = tileSize;
    }

    public static class Tile {
        int cornerX;
        int cornerY;
        int tileSize;
        public Tile(int cornerX, int cornerY, int tileSize) {
            this.cornerX = cornerX;
            this.cornerY = cornerY;
            this.tileSize = tileSize;
        }

    }

    public String getSpriteSheet() {
        return spriteSheet;
    }

    public void setSpriteSheet(String spriteSheet) {
        this.spriteSheet = spriteSheet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
