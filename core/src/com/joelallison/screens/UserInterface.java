package com.joelallison.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;

import java.lang.reflect.Method;
import java.util.HashMap;

public class UserInterface {

    private static Dialog dialog;
    private static Skin defaultSkin, commodoreSkin, chosenSkin;
    private static Stage stage;

    public static Stage generateUIStage(SpriteBatch sb, String scene) {
        defaultSkin = new Skin(Gdx.files.internal("data/defaultUI/uiskin.json"));

        commodoreSkin = new Skin(Gdx.files.internal("data/commodore64UI/uiskin.json"));

        chosenSkin = defaultSkin;

        stage = new Stage();

        Gdx.input.setInputProcessor(stage);

        switch(scene) {
            case "game":
                gameStage(scene);
                break;
            case "login":
                loginStage(scene);
                break;
        }

        return stage;
    }

    private static void loginStage(String scene) {
        final Label title = new Label("Procedural Level Gen Tool", commodoreSkin, "default");
        title.setFontScale(2, 2);
        title.setX((Gdx.graphics.getWidth() * 0.5f) - (title.getWidth() / 2));
        title.setY(Gdx.graphics.getHeight() * 0.8f);

        stage.addActor(title);
    }

    private static void gameStage(String scene) {
        //toolbar
        final TextButton file = new TextButton("File", chosenSkin, "default");
        file.setSize(55f, 25f);
        file.setPosition(0, Gdx.graphics.getHeight() - file.getHeight());

        file.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                file.setText("Bingus!");
            }
        });

        stage.addActor(file);

        final TextButton edit = new TextButton("Edit", chosenSkin, "default");
        edit.setSize(55f, 25f);
        edit.setPosition(0 + file.getWidth(), Gdx.graphics.getHeight() - edit.getHeight());

        edit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                edit.setText("Bingus?");
            }
        });

        stage.addActor(edit);




        //box to edit parameters of generation
        final Window parameters = new Window("Edit parameters", chosenSkin);
        parameters.setSize(100f, 100f);
        parameters.setPosition(100, 500);
        parameters.setMovable(false);

        stage.addActor(parameters);




    }



}
