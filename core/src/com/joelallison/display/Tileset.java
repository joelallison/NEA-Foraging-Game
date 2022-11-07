package com.joelallison.display;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.joelallison.generation.FileHandling;

import java.util.HashMap;




import static com.badlogic.gdx.graphics.g2d.ParticleEmitter.SpawnShape.line;

public class Tileset {
    private String name;
    private Texture spriteSheet;
    public int tileSize;
    public HashMap<String, TextureRegion> map = new HashMap<String, TextureRegion>();

    public Tileset(String name, Texture spriteSheet, int tileSize) {
        this.name = name;
        this.spriteSheet = spriteSheet;
        this.tileSize = tileSize;
    }

    public void initFromJSON(String filename) {

    }

    public Texture getSpriteSheet() {
        return spriteSheet;
    }

    public void setSpriteSheet(Texture spriteSheet) {
        this.spriteSheet = spriteSheet;
    }

}
