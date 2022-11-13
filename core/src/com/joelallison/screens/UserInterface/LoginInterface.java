package com.joelallison.screens.UserInterface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class LoginInterface extends UserInterface{
    public static void genUI() {
        final Label title = new Label("Procedural Level Gen Tool", chosenSkin, "default");
        title.setFontScale(2, 2);
        title.setX((Gdx.graphics.getWidth() * 0.5f) - (title.getWidth() / 2));
        title.setY(Gdx.graphics.getHeight() * 0.8f);

        stage.addActor(title);
    }
}
