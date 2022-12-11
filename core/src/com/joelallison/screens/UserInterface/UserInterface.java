package com.joelallison.screens.UserInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class UserInterface {
    protected static Stage stage;
    protected static Skin chosenSkin = new Skin(Gdx.files.internal("data/defaultUI/uiskin.json"));
    protected static String[] values = new String[20];

    public Stage genStage() {
        setupSkins();
        valuesDeclaration();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        return stage;
    }

    private void setupSkins(){
        Skin defaultSkin = new Skin(Gdx.files.internal("data/defaultUI/uiskin.json")); //so that I could later implement a skin switching system

        chosenSkin = defaultSkin;
    }

    public String[] getValues() {
        return values;
    }

    protected void valuesDeclaration() {

    }



}
