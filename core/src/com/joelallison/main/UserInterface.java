package com.joelallison.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class UserInterface {
    private static Skin defaultSkin, commodoreSkin, chosenSkin;
    private static Stage stage;

    public static Stage generateUIStage(SpriteBatch sb, String scene) {
        defaultSkin = new Skin(Gdx.files.internal("data/defaultUI/uiskin.json"));
        commodoreSkin = new Skin(Gdx.files.internal("data/commodore64UI/uiskin.json"));

        chosenSkin = defaultSkin;

        stage = new Stage();

        switch(scene) {
            case "game":
                gameStage(scene);
                break;
            case "login":
                loginStage(scene);
                break;
        }

        Gdx.input.setInputProcessor(stage);


        return stage;
    }

    private static void loginStage(String scene) {
        final Label title = new Label("Procedural Level Gen Tool", commodoreSkin, "default");
        title.setFontScale(2, 2);
        title.setX(Gdx.graphics.getWidth() * 0.5f);
        title.setY(Gdx.graphics.getHeight() * 0.8f);

        stage.addActor(title);
    }

    private static void gameStage(String scene) {
        final TextButton file = new TextButton("File", chosenSkin, "default");
        file.setSize(55f, 25f);
        file.setPosition(0, Gdx.graphics.getHeight() - file.getHeight());

        file.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                file.setText("Bingus!");
            }
        });

        stage.addActor(file);

        final TextButton edit = new TextButton("Edit", chosenSkin, "default");
        edit.setSize(55f, 25f);
        edit.setPosition(0 + file.getWidth(), Gdx.graphics.getHeight() - edit.getHeight());

        edit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                edit.setText("Bingus?");
            }
        });

        stage.addActor(edit);
    }
}
