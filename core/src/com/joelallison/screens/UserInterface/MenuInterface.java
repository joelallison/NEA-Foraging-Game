package com.joelallison.screens.UserInterface;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.joelallison.screens.MainScreen;


public class MenuInterface extends UserInterface {

    Label usernameLabel = new Label("Username", chosenSkin);
    TextField usernameField = new TextField("", chosenSkin);
    Label passwordLabel = new Label("Password", chosenSkin);
    TextField passwordField = new TextField("", chosenSkin);
    TextButton loginButton = new TextButton("Login", chosenSkin);

    TextButton skipButton = new TextButton("Just do the thing", chosenSkin);
    Table loginTable = new Table();

    public void genUI(final Stage stage) { //stage is made final here so that it can be accessed within inner classes
        // TextFields don't lose focus by default when you click out, so...
        stage.getRoot().addCaptureListener(new InputListener() {
            // this code was gratefully found on a GitHub forum --> https://github.com/libgdx/libgdx/issues/2173
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!(event.getTarget() instanceof TextField)) stage.setKeyboardFocus(null);
                return false;
            }
        });

        skipButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainScreen());
                return true;
            }
        });

        //loginTable.setDebug(true);
        loginTable.add(usernameLabel);
        loginTable.add(usernameField);
        loginTable.row();
        loginTable.add(passwordLabel);
        loginTable.add(passwordField);
        loginTable.add(loginButton);
        loginTable.row();
        loginTable.add(skipButton);


        loginTable.pad(16);
        loginTable.setPosition((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);

        stage.addActor(loginTable);
    }
}
