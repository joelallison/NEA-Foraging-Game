package com.joelallison.display;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class DisplayWindow {
    private Vector2 centerCoordinates = new Vector2(0, 0);
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
    private String mode;

    public DisplayWindow(Tileset chosenTileset, String[][] map, String mode) {
        this.chosenTileset = chosenTileset;
        this.map = map;
        this.mode = mode;
    }
    public void drawView() {

    }

}
