package com.joelallison.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.joelallison.screens.userInterface.LoginInterface;
import com.joelallison.user.Database;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import static com.joelallison.user.Database.doSqlQuery;

public class LoginScreen implements Screen {
    SpriteBatch batch;
    Stage menuUIStage;
    ExtendViewport viewport;
    OrthographicCamera camera;
    float stateTime;
    LoginInterface userInterface = new LoginInterface();
    private String username;

    public LoginScreen() {
        camera = new OrthographicCamera(1920, 1080);
        viewport = new ExtendViewport(1920, 1080, camera);

        batch = new SpriteBatch();
        Database.makeConnection(Database.jdbcURL, Database.username, Database.password);
    }

    @Override
    public void show() {
        menuUIStage = userInterface.genStage(menuUIStage);
        userInterface.genUI(menuUIStage);
    }

    public void render(float delta) {
        stateTime += Gdx.graphics.getDeltaTime();

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        camera.update();
        ScreenUtils.clear(Color.valueOf("#5b8550"));

        //userInterface.update(); --> there's no update method, not needed for this page

        batch.begin();

        batch.end();

        menuUIStage.act();
        menuUIStage.draw();
    }

    public static void login() {
        if (checkPassword()) {
            ((Game) Gdx.app.getApplicationListener()).setScreen(new CreationSelectScreen(LoginInterface.getUsernameField()));
        }
    }

    public static boolean checkPassword() {
        ResultSet rs = doSqlQuery("SELECT password, password_salt FROM users WHERE username = '" + LoginInterface.getUsernameField() + "'");
        try {
            if (rs.next()) { // if there is an entry with that username...
                String salt = rs.getString("password_salt");
                //checking hashed & salted password against stored hashed & salted password
                if (hashString(LoginInterface.getPasswordField(), salt).equals(rs.getString("password"))) {
                    LoginInterface.feedbackLabel.setText("Login successful.");
                    return true;
                } else {
                    //writing 'username or password', when it's clear within the code that the issue is that the username is not in the database, increases security.
                    LoginInterface.feedbackLabel.setText("Username or password is incorrect.");
                    return false;
                }
            } else {
                //writing 'username or password', when it's clear within the code that the issue is that the username is not in the database, increases security.
                LoginInterface.feedbackLabel.setText("Username or password is incorrect.");
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void register() {
        if (addNewUser()) {
            ((Game) Gdx.app.getApplicationListener()).setScreen(new CreationSelectScreen(LoginInterface.getUsernameField()));
        }
    }

    public static boolean addNewUser() {
        ResultSet nameConflict = doSqlQuery("SELECT * FROM users where username = '" + LoginInterface.getUsernameField() + "'");
        try {
            if (!nameConflict.next()) { //no rows (meaning no row with that username) will return false
                nameConflict.close();

                //regex found here: https://stackoverflow.com/questions/19605150/regex-for-password-must-contain-at-least-eight-characters-at-least-one-number-a
                if (LoginInterface.getPasswordField().matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")) {
                    // generate the salt and store it with the password
                    String salt = genSalt();
                    if (Database.doSqlStatement("INSERT INTO users (username, password, password_salt)" +
                            "VALUES ('" + LoginInterface.getUsernameField() + "', '" + hashString(LoginInterface.getPasswordField(), salt) + "', '" + salt + "')"
                    )) {
                        LoginInterface.feedbackLabel.setText("User added.");
                        return true;
                    }
                } else {
                    LoginInterface.feedbackLabel.setText("Password must have a \nminimum of eight characters, \nat least one letter, \none number and \none special character.");
                    return false;
                }
            } else {
                LoginInterface.feedbackLabel.setText("Username taken.");
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public static String hashString(String inputString, String salt) {
        //a hash implementation using SHA-512 and a salt, adapted from https://subscription.packtpub.com/book/security/9781849697767/1/ch01lvl1sec09/creating-a-strong-hash-simple
        //I found these two pages really useful for learning about salting hashes https://auth0.com/blog/adding-salt-to-hashing-a-better-way-to-store-passwords/ & https://security.stackexchange.com/questions/17421/how-to-store-salt/17435#17435

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update((salt + inputString).getBytes()); //loading the salted password into the messagedigest instance
            byte[] byteData = md.digest();
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error hashing password.");
        }

        return null;
    }

    public static String genSalt() {
        //generating a salt to add to the password
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        return (Arrays.toString(salt));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose () {
    }
}
