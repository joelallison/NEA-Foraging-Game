package com.joelallison.screens.userinterface;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.joelallison.screens.AppScreen;


public class MenuInterface extends UserInterface {

    Label usernameLabel = new Label("Username", chosenSkin);
    TextField usernameField = new TextField("", chosenSkin);
    Label passwordLabel = new Label("Password", chosenSkin);
    TextField passwordField = new TextField("", chosenSkin);
    TextButton loginButton = new TextButton("Login", chosenSkin);
    TextButton registerButton = new TextButton("Register", chosenSkin);

    TextButton skipButton = new TextButton("Go to app", chosenSkin);
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
                ((Game) Gdx.app.getApplicationListener()).setScreen(new AppScreen());
                return true;
            }
        });



        loginTable.setDebug(true);
        loginTable.setFillParent(true);

        loginTable.add(usernameLabel).space(4).align(Align.left);
        loginTable.row();
        loginTable.add(usernameField).space(4).colspan(3);
        loginTable.row();
        loginTable.add(passwordLabel).space(4).align(Align.left);;
        loginTable.row();
        loginTable.add(passwordField).space(4).colspan(3);
        loginTable.row();
        loginTable.add(registerButton).space(4);
        loginTable.add(loginButton).space(4);
        loginTable.row();
        loginTable.add(skipButton);


        // login screen
        // register screen
        // buttons to go between the two

        // user account
        // username - unique, password - hashed, meets certain requirements
        // can store save files to server

        //loginTable.setPosition((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);

        stage.addActor(loginTable);
    }
}

