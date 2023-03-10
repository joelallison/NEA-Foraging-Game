package com.joelallison.screens.userInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.joelallison.generation.Layer;
import com.joelallison.generation.MazeLayer;
import com.joelallison.generation.TerrainLayer;
import com.joelallison.graphics.Tileset;
import com.joelallison.generation.World;
import com.joelallison.io.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static com.joelallison.screens.WorldSelectScreen.loadWorldIntoApp;
import static com.joelallison.screens.WorldSelectScreen.username;

public class WorldUI extends UI {
    //this https://stackoverflow.com/a/17999317 was incredibly useful in getting the scrollpane to work
    Table containerTable = new Table();
    Table worlds = new Table();
    private static final String PATTERN_FORMAT = "dd/MM/yyyy - HH:mm";
    Dialog newworld = newWorldPopup();

    public void genUI(final Stage stage) { //stage is made final here so that it can be accessed within inner classes
        worlds.defaults().space(8);
        loadWorlds(stage);
        worlds.row();

        TextButton newworldButton = new TextButton("New World", skin);
        newworldButton.addListener(
                new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        newworld.show(stage);
                        return true;
                    }
                });

        worlds.add(newworldButton);

        worlds.setSize(worlds.getPrefWidth(), worlds.getPrefHeight());
        //worlds.setDebug(true);

        ScrollPane selectionScroll = new ScrollPane(worlds);
        selectionScroll.setSize(Gdx.graphics.getWidth() * 0.3f, Gdx.graphics.getHeight());
        selectionScroll.setPosition(Gdx.graphics.getWidth() / 2 - selectionScroll.getWidth() / 2, Gdx.graphics.getHeight() / 2 - selectionScroll.getHeight() / 2);
        selectionScroll.setScrollbarsVisible(true);
        selectionScroll.setScrollingDisabled(false, true);
        //selectionScroll.setDebug(true);

        //containerTable.add(new Label("worlds - Load", skin));
        //containerTable.row();
        //containerTable.add(selectionScroll);
        //containerTable.row();

        stage.addActor(selectionScroll);
    }

    public void loadWorlds(Stage stage) {
        //getting the metadata of the worlds so that the user can have more information about what they're choosing before they choose it.

        try {
            ResultSet getWorldsResults = Database.doSqlQuery(
                    "SELECT * FROM world " +
                            "WHERE \"username\" = '" + username + "' " +
                            "ORDER BY last_accessed_timestamp DESC;"
            );


            if (getWorldsResults.next()) {
                try {
                    do {
                        String name = getWorldsResults.getString("world_name");
                        Instant dateCreated = getWorldsResults.getTimestamp("created_timestamp").toInstant();
                        Instant lastAccessed = getWorldsResults.getTimestamp("last_accessed_timestamp").toInstant();
                        Long seed = getWorldsResults.getLong("world_seed");

                        //get number of layers which are part of this world
                        ResultSet layerCountRS = Database.doSqlQuery("SELECT COUNT(*) FROM layer WHERE world_name = '" + name + "' AND username = '" + username + "'");

                        int layerCount = 0;
                        if (layerCountRS.next()) {
                            layerCount = layerCountRS.getInt("count");
                        }

                        worlds.row();
                        worlds.add(selectWorldButton(name, dateCreated, lastAccessed, layerCount, seed));

                    } while (getWorldsResults.next());
                } catch (SQLException e) {
                    basicPopupMessage("Error!", e.getMessage(), stage);
                }
            }
        } catch (Exception e) {
            basicPopupMessage("Error!", e.getMessage(), stage);
        }
    }

    public TextButton selectWorldButton(final String name, final Instant dateCreated, final Instant lastAccessed, int layerCount, final Long seed) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT).withZone(ZoneId.systemDefault());

        TextButton button = new TextButton("World name: " + name +
                "\nDate created: " + formatter.format(dateCreated) +
                "\nLast accessed: " + formatter.format(lastAccessed) +
                "\nNumber of layers: " + Integer.toString(layerCount) +
                "\nSeed: " + Long.toString(seed), skin);

        button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //even though I could just pass name and username through, and then query to find the other details again, I think it's better to keep passing the values through
                getWorld(name, username, seed, dateCreated);
                return true;
            }
        });

        return button;
    }

    public static void getWorld(String worldName, String username, Long seed, Instant dateCreated) {
        ArrayList<Layer> layers = new ArrayList<>();

        ResultSet worldLayers = Database.doSqlQuery(
                "SELECT * FROM layer " +
                        "WHERE \"username\" = '" + username + "' " +
                        "AND \"world_name\" = '" + worldName + "'" +
                        "ORDER BY layer_number ASC;"
        );

        try {
            while(worldLayers.next()) {
                //loading all layers found from the query into the arraylist
                switch(worldLayers.getString("layer_type").charAt(0)) {
                    //need to be processed differently based on type (different constructors etc.)
                    //I could have the different types of layer-loading as methods, but I don't need to get individual layers from the database in any other place so it's pointless
                    case 'T':
                        ResultSet terrainLayerRS = Database.doSqlQuery(
                                "SELECT * FROM terrain_layer " +
                                        "WHERE \"layer_id\" = " + worldLayers.getInt("layer_id")
                        );

                        //as worldLayers.next() == true, and that layer is marked 'T', a terrain layer will be returned from the query, terrainLayerRS shouldn't be null
                        terrainLayerRS.next();

                        TerrainLayer terrainLayer = new TerrainLayer(
                                worldLayers.getInt("layer_id"),
                                worldLayers.getString("layer_name"),
                                worldLayers.getLong("seed"),
                                terrainLayerRS.getFloat("scale"),
                                terrainLayerRS.getInt("octaves"),
                                terrainLayerRS.getFloat("lacunarity"),
                                terrainLayerRS.getInt("wrap"),
                                terrainLayerRS.getBoolean("invert")
                        );

                        terrainLayer.tilesetName = worldLayers.getString("tileset_name");
                        terrainLayer.setInheritSeed(worldLayers.getBoolean("inherit_seed"));
                        terrainLayer.setCenter(new Vector2(worldLayers.getInt("center_x"), worldLayers.getInt("center_y")));

                        ResultSet terrainTileSpecsRS = Database.doSqlQuery(
                                "SELECT * FROM terrain_tile_specs " +
                                        "WHERE \"layer_id\" = " + worldLayers.getInt("layer_id")
                        );

                        while(terrainTileSpecsRS.next()) {
                            terrainLayer.tileSpecs.add(new Tileset.TerrainTileSpec(terrainTileSpecsRS.getString("tile_name"), terrainTileSpecsRS.getFloat("lower_bound")));
                        }

                        System.out.println(terrainLayer);
                        layers.add(terrainLayer);
                        break;
                    case 'M':
                        ResultSet mazeLayerRS = Database.doSqlQuery(
                                "SELECT * FROM maze_layer " +
                                        "WHERE \"layer_id\" = " + worldLayers.getInt("layer_id")
                        );

                        //as worldLayers.next() == true, and that layer is marked 'M', a maze layer will be returned from the query, mazeLayerRS shouldn't be null
                        mazeLayerRS.next();

                        MazeLayer mazeLayer
                                = new MazeLayer(
                                mazeLayerRS.getInt("layer_id"),
                                worldLayers.getString("layer_name"),
                                worldLayers.getLong("seed"),
                                mazeLayerRS.getInt("width"),
                                mazeLayerRS.getInt("height"),
                                worldLayers.getString("tileset_name"),
                                mazeLayerRS.getBoolean("opaque")
                        );



                        mazeLayer.setInheritSeed(worldLayers.getBoolean("inherit_seed"));
                        mazeLayer.setCenter(new Vector2(worldLayers.getInt("center_x"), worldLayers.getInt("center_y")));



                        ResultSet mazeTileSpecsRS = Database.doSqlQuery(
                                "SELECT * FROM maze_tile_specs " +
                                        "WHERE \"layer_id\" = " + worldLayers.getInt("layer_id")
                        );

                        while(mazeTileSpecsRS.next()) {
                            mazeLayer.tileSpecs.add(new Tileset.MazeTileSpec(mazeTileSpecsRS.getString("tile_name"), Tileset.MazeTileSpec.neighbourMapParseString(mazeTileSpecsRS.getString("neighbour_map"))));
                        }

                        layers.add(mazeLayer);
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        World world = new World(worldName, layers, seed, dateCreated);
        loadWorldIntoApp(world, username);
    }


    public static Dialog newWorldPopup() {
        final Dialog popupBox = new Dialog("New creation", skin);

        Table table = new Table();
        //table.setDebug(true);
        table.setFillParent(true);
        table.defaults().align(Align.left).space(8);
        table.pad(16);

        final Label nameLabel = new Label("Name:", skin);
        table.add(nameLabel).colspan(100); //colspan is to allow the 'go' and 'cancel' buttons to be a lot closer together
        final TextField nameField = new TextField("", skin);
        table.add(nameField);

        table.row();
        final Label seedLabel = new Label("Seed: (leave blank for random) ", skin);
        table.add(seedLabel).colspan(100); //colspan is to allow the 'go' and 'cancel' buttons to be a lot closer together
        final TextField seedField = new TextField("", skin);
        table.add(seedField);

        table.row();
        TextButton goButton = new TextButton("Go", skin);
        goButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!nameField.getText().equals("")) {
                    if (seedField.getText().matches("[0-9]*")) {
                        if (seedField.getText().equals("")) {
                            loadWorldIntoApp(new World(nameField.getText()), username);
                        } else {
                            loadWorldIntoApp(new World(nameField.getText(), Long.parseLong(seedField.getText())), username);
                        }
                    } else {
                        seedLabel.setText("Seed: (leave blank for random)\nMust be a positive int.");
                    }
                } else {
                    nameLabel.setText("Name: (must not be left blank!)");
                }
                return true;
            }
        });

        table.add(goButton);
        TextButton cancelButton = new TextButton("Cancel", skin);
        cancelButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                popupBox.cancel();
                return true;
            }
        });
        table.add(cancelButton);

        popupBox.add(table);

        return popupBox;
    }

}

