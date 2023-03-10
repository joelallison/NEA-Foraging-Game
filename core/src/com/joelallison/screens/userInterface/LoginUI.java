package com.joelallison.screens.userInterface;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.joelallison.screens.AppScreen;
import com.joelallison.generation.World;

import static com.joelallison.screens.LoginScreen.login;
import static com.joelallison.screens.LoginScreen.register;


public class LoginUI extends UI {

    Label usernameLabel = new Label("Username", skin);
    static TextField usernameField = new TextField("", skin);
    Label passwordLabel = new Label("Password", skin);
    static TextField passwordField = new TextField("", skin);
    TextButton loginButton = new TextButton("Login", skin);
    TextButton registerButton = new TextButton("Register", skin);
    TextButton skipButton = new TextButton("Go to app", skin);
    public static Label feedbackLabel = new Label("", skin);
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

        Image title = new Image(new Texture(Gdx.files.internal("title.png")));
        //title.setAlign(Align.center);
        title.setPosition(Gdx.graphics.getWidth() / 2 - title.getPrefWidth() / 2, 200 + Gdx.graphics.getHeight() / 2 - title.getPrefHeight() / 2);
        stage.addActor(title);

        Image name = new Image(new Texture(Gdx.files.internal("name.png")));
        //title.setAlign(Align.center);
        name.setPosition(Gdx.graphics.getWidth() / 2 - name.getPrefWidth() / 2, (Gdx.graphics.getHeight() / 2 - (name.getPrefHeight() / 2)) - 200);
        stage.addActor(name);


        createLoginTable();
        stage.addActor(loginTable);
    }

    public void createLoginTable() {
        //loginTable.setDebug(true);
        loginTable.setFillParent(true);
        loginTable.center();

        loginTable.defaults().align(Align.left).space(4);

        loginTable.add(usernameLabel);

        loginTable.row();
        loginTable.add(usernameField).colspan(2);

        loginTable.row();
        loginTable.add(passwordLabel);

        loginTable.row();
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        loginTable.add(passwordField).colspan(2);

        loginTable.row();
        registerButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                register();
                return true;
            }
        });
        loginTable.add(registerButton);

        loginButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                login();
                return true;
            }
        });

        loginTable.add(loginButton);
        loginTable.row();

        skipButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                ((Game) Gdx.app.getApplicationListener()).setScreen(new AppScreen(new World("new"), ""));
                return true;
            }
        });
        loginTable.add(skipButton);
        loginTable.row();
        loginTable.add(feedbackLabel).colspan(2);
    }

    public static String getUsernameField() {
        return usernameField.getText();
    }

    public static String getPasswordField() {
        return passwordField.getText();
    }
}

