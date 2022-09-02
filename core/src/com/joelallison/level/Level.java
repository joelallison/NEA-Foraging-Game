package com.joelallison.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Level {

    public TileType[] tilesToGen = new TileType[3];

    public Level() {



        //tree generation
        tilesToGen[0] = new TileType("tree", 1, false, 8, 2, 1.55f, 1.1f, -1, true);
        tilesToGen[0].bounds = new float[] {0.38f, 0.4f, 0.6f, 0.7f};
        tilesToGen[0].setSpriteSheet(new Texture(Gdx.files.internal("tree_tileSheet.png")));
        tilesToGen[0].sprites = new TextureRegion[] {new TextureRegion(tilesToGen[0].getSpriteSheet(), 0, 0, 8, 8), //plant
                new TextureRegion(tilesToGen[0].getSpriteSheet(), 8, 0, 8, 8), //bush
                new TextureRegion(tilesToGen[0].getSpriteSheet(), 16, 0, 8, 8), //dark green tree
                new TextureRegion(tilesToGen[0].getSpriteSheet(), 24, 0, 8, 8)}; //light green tree

        //rocks generation
        tilesToGen[1] = new TileType("rock", 2, true, 1, 2, 1.3f, 6f, 2, true);
        tilesToGen[1].bounds = new float[] {0.945f, 0.99f};
        tilesToGen[1].setSpriteSheet(new Texture(Gdx.files.internal("rock_tileSheet.png")));
        tilesToGen[1].sprites = new TextureRegion[] {new TextureRegion(tilesToGen[1].getSpriteSheet(), 0, 0, 8, 8), //small rock
                new TextureRegion(tilesToGen[1].getSpriteSheet(), 8, 0, 8, 8)}; //big rock
    }

    public static String[][] compositeLevel() {

        return new String[0][];
    }
}
