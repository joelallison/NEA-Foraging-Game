package com.joelallison.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.joelallison.generation.MazeLayer;
import com.joelallison.graphics.Tileset;
import com.joelallison.generation.Layer;
import com.joelallison.generation.World;
import com.joelallison.generation.TerrainLayer;
import com.joelallison.screens.userInterface.AppUI;

import java.util.Arrays;
import java.util.HashMap;

public class AppScreen implements Screen {
    Stage mainUIStage;
    //viewport displays the generated tiles
    public ExtendViewport viewport;
    public static OrthographicCamera camera;
    SpriteBatch batch;
    ShapeRenderer sr; //for misc UI additions
    public static World world;
    public static String username;
    public static final int TILE_SIZE = 32;
    public static final int CHUNK_SIZE = 14;
    public static final Vector2 LEVEL_ASPECT_RATIO = new Vector2(4, 3);
    public static int levelAspectScalar = 2;
    public static Vector2 mapDimensions = new Vector2((int) CHUNK_SIZE * levelAspectScalar * LEVEL_ASPECT_RATIO.x, (int) CHUNK_SIZE * levelAspectScalar * LEVEL_ASPECT_RATIO.y);
    Texture missing_tile = new Texture(Gdx.files.internal("missing_tile.png"));
    public static int xPos, yPos;
    public static UserInput userInput;
    float stateTime;
    public static HashMap<String, Tileset> tilesets;
    AppUI userInterface = new AppUI();

    public AppScreen(World world, String username) {
        AppScreen.world = world;
        this.username = username;

        camera = new OrthographicCamera(1920, 1080);
        viewport = new ExtendViewport(1920, 1080, camera);

        batch = new SpriteBatch();
        sr = new ShapeRenderer();

        //declare player stuff
        userInput = new UserInput(0, 0);
    }

    @Override
    public void show() {
        camera.zoom = 2.4f; //default viewport size
        viewport.apply(true);

        // declare default coords
        xPos = 0;
        yPos = 0;

        stateTime = 0f;

        mainUIStage = userInterface.genStage(mainUIStage);
        userInterface.genUI(mainUIStage);
    }

    @Override
    public void hide() {
        userInterface.clearUI();
    }

    @Override
    public void render(float delta) {
        stateTime += Gdx.graphics.getDeltaTime();
        viewport.apply();
        camera.zoom = MathUtils.clamp(camera.zoom, 0.2f, 4); //set limits for the size of the viewport
        batch.setProjectionMatrix(viewport.getCamera().combined);
        sr.setProjectionMatrix(viewport.getCamera().combined);
        camera.update();

        userInput.handleInput();
        xPos = userInput.getxPosition();
        yPos = userInput.getyPosition();

        ScreenUtils.clear(world.getClearColor());


        batch.begin();
        drawLayers();
        //drawLayers();
        batch.end();

        drawUIShapes();

        //update the ui and draw it on top of everything else
        world.makeLayerNamesUnique();
        userInterface.update(delta);
        mainUIStage.draw();
        mainUIStage.act();
    }

    public void drawUIShapes() {
        sr.begin(ShapeRenderer.ShapeType.Line);

        //draw border around the generated outcome
        sr.setColor(Color.WHITE);
        sr.rect(0f, 0f, mapDimensions.x * TILE_SIZE, mapDimensions.y * TILE_SIZE);

        sr.end();
    }

    public void drawLayers() {
        //generate
        for (int i = 0; i < world.layers.size(); i++) {
            switch(getLayerTypeChar(world.layers.get(i))) {
                case 'T':
                    ((TerrainLayer) world.layers.get(i)).genValueMap(world.getLayerSeed(i), mapDimensions, xPos + (int) (world.layers.get(i).getCenter().x), yPos + (int) (world.layers.get(i).getCenter().y));
                    break;
                case 'M':
                    ((MazeLayer) world.layers.get(i)).genMaze(world.getLayerSeed(i));
            }
        }

        //draw
        boolean[][] tileAbove = new boolean[(int) mapDimensions.x][(int) mapDimensions.y];
        for (int x = 0; x < mapDimensions.x; x++) {
            for (int y = 0; y < mapDimensions.y; y++) {
                // top layer to bottom layer
                for (int i = world.layers.size() - 1; i >= 0; i--) {
                    if (world.layers.get(i).layerShown()) {
                        if (tileAbove[x][y] == false) { // no need to draw if there's already a tile above
                                switch (getLayerTypeChar(world.layers.get(i))) {
                                    case 'T':
                                        TextureRegion terrainTile = getTextureForTerrainValue(world.layers.get(i), x, y);
                                        if (terrainTile != null) {
                                            batch.draw(terrainTile, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                                            tileAbove[x][y] = true;
                                        }
                                        break;
                                    case 'M':
                                        //if x is within maze
                                        if (x + xPos < (world.layers.get(i)).getCenter().x + ((MazeLayer) world.layers.get(i)).getWidth()+1 && (x + xPos > (world.layers.get(i)).getCenter().x)) {
                                            //if y is within maze
                                            if (y + yPos < (world.layers.get(i)).getCenter().y + ((MazeLayer) world.layers.get(i)).getHeight()+1 && (y + yPos > (world.layers.get(i)).getCenter().y)) {
                                                if (((MazeLayer) world.layers.get(i)).maze[(int) (y + yPos - world.layers.get(i).getCenter().y-1)][(int) (x + xPos - world.layers.get(i).getCenter().x-1)] == 1) {
                                                    TextureRegion mazeTile = getTextureForNeighbourMap(world.layers.get(i), ((int) (x + xPos - world.layers.get(i).getCenter().x-1)), (int) (((MazeLayer) world.layers.get(i)).getHeight() - (y + yPos - world.layers.get(i).getCenter().y-1) - 1));
                                                    if (mazeTile != null) {
                                                        batch.draw(mazeTile, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                                                        tileAbove[x][y] = true;
                                                    }
                                                } else if (((MazeLayer) world.layers.get(i)).isOpaque()) {
                                                    tileAbove[x][y] = true;
                                                }
                                            }
                                        }
                                        break;
                                }
                        }
                    }
                }

            }
        }
    }

    public static TextureRegion getTextureForNeighbourMap(Layer layer, int x, int y) {
        String tileName = getTileNameForNeighbourMap(layer, getNeighbourMap(layer, x, y));
        if (!tileName.equals("-")){
            return tilesets.get(layer.tilesetName).getTileTextureFromName(tileName);
        } else {
            return null;
        }
    }


    public static String getTileNameForNeighbourMap(Layer layer, boolean[][] map) {
        for (int i = 0; i < ((MazeLayer) layer).tileSpecs.size(); i++) {
            if (Arrays.deepEquals(((MazeLayer) layer).tileSpecs.get(i).neighbourMap, map)) {
                return ((MazeLayer) layer).tileSpecs.get(i).name;
            }
        }

        return "-";
    }
    public static boolean[][] getNeighbourMap(Layer layer, int y, int x) {
        boolean[][] neighbourMap = new boolean[3][3];
        //top to bottom, left to right
        try {
            for (int row = y + 1; row > y - 2; row--) {
                for (int col = x - 1; col < x + 2; col++) {
                    if (((row >= 0)) && (row < ((MazeLayer) layer).getHeight()) && ((col >= 0)) && (col < ((MazeLayer) layer).getWidth())) {
                        neighbourMap[col - x + 1][row - y + 1] = !((((MazeLayer) layer).maze[((MazeLayer) layer).maze[0].length - col - 1][row]) == 0);
                    } else {
                        //if either row or column is out of bounds of the maze
                        neighbourMap[col - x + 1][row - y + 1] = false;
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
            //when maze size is changed, the maze.length is then out of date, and the program crashes
        }


        //if any one of the corners is false, set them all to false
        //-- this is because there is no actual tile where you would want an L-shaped mapping,
        //but there are still tiles which are identified by having corners true
        //e.g. a tile with tiles ALL around it
        if (!neighbourMap[0][0] || !neighbourMap[0][2] || !neighbourMap[2][0] || !neighbourMap[2][2]) {
            neighbourMap[0][0] = false;
            neighbourMap[0][2] = false;
            neighbourMap[2][0] = false;
            neighbourMap[2][2] = false;
        }


        return neighbourMap;
    }

    public static TextureRegion getTextureForTerrainValue(Layer layer, int x, int y) {
        String tileName = getTileNameForTerrainValue(layer, x, y);
        if (!tileName.equals("-")){
            return tilesets.get(layer.tilesetName).getTileTextureFromName(tileName);
        } else {
            return null; // if there should be no tile drawn at this position for this layer
        }
    }

    public static String getTileNameForTerrainValue(Layer layer, int x, int y) {
        for (int i = 0; i < ((TerrainLayer) layer).tileSpecs.size(); i++) {
            if (((TerrainLayer) layer).valueMap[x][y] > ((TerrainLayer) layer).tileSpecs.get(i).lowerBound) {
                return ((TerrainLayer) layer).tileSpecs.get(i).name;
            }
        }

        return "-"; //if there's nothing to be drawn here, return this
    }

    public static char getLayerTypeChar(Layer layer) {
        return getLayerTypeString(layer).charAt(0);
    }

    public static String getLayerTypeString(Layer layer) {
        return layer.getClass().getName().replace("com.joelallison.generation.", "").replace("Layer", "");
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(mapDimensions.x * TILE_SIZE / 2, mapDimensions.y * TILE_SIZE / 2, 0);
        viewport.getCamera().update();
    }

    @Override
    public void dispose() {
        sr.dispose();
        batch.dispose();
        for (Layer layer : world.layers) {
            tilesets.get(layer.tilesetName).getSpritesheetTexture().dispose();
        }
    }
}