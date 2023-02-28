package com.joelallison.screens.userInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.joelallison.screens.WorldSelectScreen;
import com.joelallison.user.World;
import com.joelallison.user.Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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

        TextButton newworldButton = new TextButton("New World", chosenSkin);
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

        //containerTable.add(new Label("worlds - Load", chosenSkin));
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
                "\nSeed: " + Long.toString(seed), chosenSkin);

        button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //even though I could just pass name and username through, and then query to find the other details again, I think it's better to keep passing the values through
                WorldSelectScreen.getWorld(name, username, seed, dateCreated);
                return true;
            }
        });

        return button;
    }

    public static Dialog newWorldPopup() {
        final Dialog popupBox = new Dialog("New creation", chosenSkin);

        Table table = new Table();
        //table.setDebug(true);
        table.setFillParent(true);
        table.defaults().align(Align.left).space(8);
        table.pad(16);

        Label nameLabel = new Label("Name:", chosenSkin);
        table.add(nameLabel).colspan(100); //colspan is to allow the 'go' and 'cancel' buttons to be a lot closer together
        final TextField nameField = new TextField("", chosenSkin);
        table.add(nameField);

        table.row();
        Label seedLabel = new Label("Seed: (leave blank for random) ", chosenSkin);
        table.add(seedLabel).colspan(100); //colspan is to allow the 'go' and 'cancel' buttons to be a lot closer together
        final TextField seedField = new TextField("", chosenSkin);
        table.add(seedField);

        table.row();
        TextButton goButton = new TextButton("Go", chosenSkin);
        goButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (seedField.getText().equals("")) {
                    loadWorldIntoApp(new World(nameField.getText()), username);
                } else {
                    loadWorldIntoApp(new World(nameField.getText(), Long.parseLong(seedField.getText())), username);
                }

                return true;
            }
        });

        table.add(goButton);
        TextButton cancelButton = new TextButton("Cancel", chosenSkin);
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

