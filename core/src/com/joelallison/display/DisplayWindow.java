package com.joelallison.display;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class DisplayWindow {
    private Vector2 coordinates = new Vector2(0, 0);
    private Vector2 chunkCoords;
    private Tileset chosenTileset;

    private final int DEFAULT_TILE_SIZE = 16;
    private int tileScale = 2;

    private final int CHUNK_SIZE = 7;
    private final Vector2 ASPECT_RATIO = new Vector2(4, 3);
    private final int ASPECT_SCALAR = 4;

    private final float SCALE = 1;

    private final float zoomMin = 0.2f;
    private final float zoomMax = 2f;

    private boolean showPlayer = true;

    private FitViewport viewport;
    private OrthographicCamera camera;

    private String[][] map;

    private Tileset[] tilesets = new Tileset[3];

    public DisplayWindow(Tileset chosenTileset, String[][] map) {
        this.chosenTileset = chosenTileset;
        this.map = map;

        tilesets[0] = new Tileset("Trees & Rocks", new Texture(Gdx.files.internal("tree_tileSheet.png")), 8);
        tilesets[0].map.put("ground", new TextureRegion(tilesets[0].getSpriteSheet(), 0, 0, tilesets[0].tileSize, tilesets[0].tileSize));
        tilesets[0].map.put("plant", new TextureRegion(tilesets[0].getSpriteSheet(), 8, 0, tilesets[0].tileSize, tilesets[0].tileSize));
        tilesets[0].map.put("bush", new TextureRegion(tilesets[0].getSpriteSheet(), 16, 0, tilesets[0].tileSize, tilesets[0].tileSize));
        tilesets[0].map.put("tree_1", new TextureRegion(tilesets[0].getSpriteSheet(), 24, 0, tilesets[0].tileSize, tilesets[0].tileSize));
        tilesets[0].map.put("tree_2", new TextureRegion(tilesets[0].getSpriteSheet(), 32, 0, tilesets[0].tileSize, tilesets[0].tileSize));
        tilesets[0].map.put("rock_1", new TextureRegion(tilesets[0].getSpriteSheet(), 40, 0, tilesets[0].tileSize, tilesets[0].tileSize));
        tilesets[0].map.put("rock_2", new TextureRegion(tilesets[0].getSpriteSheet(), 48, 0, tilesets[0].tileSize, tilesets[0].tileSize));

        tilesets[1] = new Tileset("Kenney Micro Roguelike COLOUR", new Texture(Gdx.files.internal("tree_tileSheet.png")), 8);
    }
    public void drawView() {
        chunkCoords = new Vector2(customRound(coordinates.x, CHUNK_SIZE), customRound(coordinates.y, CHUNK_SIZE));

    }

    private int customRound(float input, int multiple) {
        return multiple * Math.round(input/multiple);
    }

    private Vector2 nextChunk() {

        return new Vector2();
    }
    private void updateArray(Vector2 direction) {

    }

}
