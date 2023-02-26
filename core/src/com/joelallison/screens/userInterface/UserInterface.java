package com.joelallison.screens.userInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;

public class UserInterface {

    public static Color defaultBackgroundColor = new Color(0.1215686f, 0.09411765f, 0.07843137f, 1);
    public static Skin chosenSkin = new Skin(Gdx.files.internal("data/defaultUI/uiskin.json"));

    public Stage genStage(Stage stage) {
        setupSkins();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        return stage;
    }

    private void setupSkins(){
        Skin defaultSkin = new Skin(Gdx.files.internal("data/defaultUI/uiskin.json"));
        chosenSkin = defaultSkin;
    }

    public void basicPopupMessage(String title, String message, final Stage stage) {
        final Dialog dialog = new Dialog(title, chosenSkin) {public void result(Object obj) {/*nothing is needed here*/}};
        dialog.text(message);
        dialog.button("OK", true);

        dialog.show(stage);
    }
}
