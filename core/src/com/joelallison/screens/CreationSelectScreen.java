package com.joelallison.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.joelallison.generation.Layer;
import com.joelallison.generation.MazeLayer;
import com.joelallison.generation.TerrainLayer;
import com.joelallison.graphics.Tileset;
import com.joelallison.screens.userInterface.CreationSelectInterface;
import com.joelallison.user.Creation;
import com.joelallison.user.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;

import static com.joelallison.user.Database.closeConnection;

public class CreationSelectScreen implements Screen {
    SpriteBatch batch;
    Stage menuUIStage;
    ExtendViewport viewport;
    OrthographicCamera camera;
    float stateTime;
    CreationSelectInterface userInterface = new CreationSelectInterface();
    public static String username;

    public CreationSelectScreen(String username) {
        camera = new OrthographicCamera(1920, 1080);
        viewport = new ExtendViewport(1920, 1080, camera);

        batch = new SpriteBatch();

        this.username = username;

        menuUIStage = userInterface.genStage(menuUIStage);
        userInterface.genUI(menuUIStage);
    }

    @Override
    public void show() {
    }

    public void render(float delta) {
        stateTime += Gdx.graphics.getDeltaTime();

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        camera.update();
        ScreenUtils.clear(new Color(0.365f, 0.525f, 0.310f, 1f));

        //userInterface.update(); --> there's no update method yet

        batch.begin();
        batch.end();

        menuUIStage.act(stateTime);
        menuUIStage.draw();
    }

    public static void getCreation(String creationName, String username, Long seed, Instant dateCreated) {
        ArrayList<Layer> layers = new ArrayList<>();

        ResultSet creationLayers = Database.doSqlQuery(
                "SELECT * FROM layer " +
                        "WHERE \"username\" = '" + username + "' " +
                        "AND \"creation_name\" = '" + creationName + "'" +
                        "ORDER BY layer_number ASC;"
        );

        try {
            while(creationLayers.next()) {
                //loading all layers found from the query into the arraylist
                switch(creationLayers.getString("layer_type").charAt(0)) {
                    //need to be processed differently based on type (different constructors etc.)
                    //I could have the different types of layer-loading as methods, but I don't need to get individual layers from the database in any other place so it's pointless
                    case 'T':
                        ResultSet terrainLayerRS = Database.doSqlQuery(
                                "SELECT * FROM terrain_layer " +
                                        "WHERE \"layer_id\" = " + creationLayers.getInt("layer_id")
                        );

                        //as creationLayers.next() == true, and that layer is marked 'T', a terrain layer will be returned from the query, terrainLayerRS shouldn't be null
                        terrainLayerRS.next();

                        TerrainLayer terrainLayer = new TerrainLayer(
                                creationLayers.getString("layer_name"),
                                creationLayers.getLong("seed"),
                                terrainLayerRS.getFloat("scale"),
                                terrainLayerRS.getInt("octaves"),
                                terrainLayerRS.getFloat("lacunarity"),
                                terrainLayerRS.getInt("wrap"),
                                terrainLayerRS.getBoolean("invert")
                                );

                        terrainLayer.setInheritSeed(creationLayers.getBoolean("inherit_seed"));
                        terrainLayer.setCenter(new Vector2(creationLayers.getInt("center_x"), creationLayers.getInt("center_y")));

                        ResultSet terrainTileSpecsRS = Database.doSqlQuery(
                                "SELECT * FROM terrain_tile_specs " +
                                        "WHERE \"layer_id\" = " + creationLayers.getInt("layer_id")
                        );

                        while(terrainTileSpecsRS.next()) {
                            terrainLayer.tileSpecs.add(new Tileset.TerrainTileSpec(terrainTileSpecsRS.getString("tile_name"), terrainTileSpecsRS.getFloat("lower_bound")));
                        }

                        layers.add(terrainLayer);
                        break;
                    case 'M':
                        ResultSet mazeLayerRS = Database.doSqlQuery(
                                "SELECT * FROM maze_layer " +
                                        "WHERE \"layer_id\" = " + creationLayers.getInt("layer_id")
                        );

                        //as creationLayers.next() == true, and that layer is marked 'M', a maze layer will be returned from the query, mazeLayerRS shouldn't be null
                        mazeLayerRS.next();

                        MazeLayer mazeLayer = new MazeLayer(
                                creationLayers.getString("name"),
                                creationLayers.getLong("seed"),
                                mazeLayerRS.getInt("width"),
                                mazeLayerRS.getInt("height")

                        );

                        mazeLayer.setInheritSeed(creationLayers.getBoolean("inherit_seed"));
                        mazeLayer.setCenter(new Vector2(creationLayers.getInt("center_x"), creationLayers.getInt("center_y")));

                        ResultSet mazeTileSpecsRS = Database.doSqlQuery(
                                "SELECT * FROM maze_tile_specs " +
                                        "WHERE \"layer_id\" = " + creationLayers.getInt("layer_id")
                        );

                        while(mazeTileSpecsRS.next()) {
                            mazeLayer.tileSpecs.add(new Tileset.MazeTileSpec(mazeTileSpecsRS.getString("tile_name"), mazeTileSpecsRS.getInt("orientation_id")));
                        }

                        layers.add(mazeLayer);
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        Creation creation = new Creation(creationName, layers, seed, dateCreated);
        loadCreationIntoApp(creation, username);
    }

    public static void loadCreationIntoApp(Creation creation, String username) {
        ((Game) Gdx.app.getApplicationListener()).setScreen(new AppScreen(creation, username));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose () {
    }
}

