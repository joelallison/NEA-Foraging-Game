package com.joelallison.screens.userInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class UI {

    public static Color defaultBackgroundColor = new Color(0.1215686f, 0.09411765f, 0.07843137f, 1);
    public static Skin skin = new Skin(Gdx.files.internal("data/defaultUI/uiskin.json"));

    public Stage genStage(Stage stage) {
        setupSkins();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        return stage;
    }

    private void setupSkins() {
        //in theory could add an option to change UI skin later
        Skin defaultSkin = new Skin(Gdx.files.internal("data/defaultUI/uiskin.json"));
        skin = defaultSkin;
    }

    public void basicPopupMessage(String title, String message, final Stage stage) {
        final Dialog dialog = new Dialog(title, skin) {public void result(Object obj) {/*nothing is needed here*/}};
        dialog.text(message);
        dialog.button("OK", true);

        dialog.show(stage);
    }
}
