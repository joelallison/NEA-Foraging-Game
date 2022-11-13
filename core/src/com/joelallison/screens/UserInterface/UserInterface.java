package com.joelallison.screens.UserInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public abstract class UserInterface {
    protected static Stage stage;
    protected static Skin chosenSkin;
    protected static String[] values = new String[20];

    public static Stage genStage() {
        values[0] = "0.2f";
        values[1] = "2";

        setupSkins();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        return stage;
    }

    private static void setupSkins(){
        Skin defaultSkin = new Skin(Gdx.files.internal("data/defaultUI/uiskin.json"));

        Skin commodoreSkin = new Skin(Gdx.files.internal("data/commodore64UI/uiskin.json"));

        chosenSkin = defaultSkin;
    }

    private static void loginStage() {

    }

    private static void gameStage(String scene) {

    }

    public static String[] getValues() {
        return values;
    }



}
