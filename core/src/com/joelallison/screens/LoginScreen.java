package com.joelallison.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.joelallison.screens.userInterface.LoginUI;
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
    LoginUI userInterface = new LoginUI();
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
            ((Game) Gdx.app.getApplicationListener()).setScreen(new WorldSelectScreen(LoginUI.getUsernameField()));
        }
    }

    public static boolean checkPassword() {
        ResultSet rs = doSqlQuery("SELECT password, password_salt FROM users WHERE username = '" + LoginUI.getUsernameField() + "'");
        try {
            if (rs.next()) { // if there is an entry with that username...
                String salt = rs.getString("password_salt");
                //checking hashed & salted password against stored hashed & salted password
                if (hashString(LoginUI.getPasswordField(), salt).equals(rs.getString("password"))) {
                    LoginUI.feedbackLabel.setText("Login successful.");
                    return true;
                } else {
                    //writing 'username or password', when it's clear within the code that the issue is that the username is not in the database, increases security.
                    LoginUI.feedbackLabel.setText("Username or password is incorrect.");
                    return false;
                }
            } else {
                //writing 'username or password', when it's clear within the code that the issue is that the username is not in the database, increases security.
                LoginUI.feedbackLabel.setText("Username or password is incorrect.");
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void register() {
        if (addNewUser()) {
            ((Game) Gdx.app.getApplicationListener()).setScreen(new WorldSelectScreen(LoginUI.getUsernameField()));
        }
    }

    public static boolean addNewUser() {
            if (nameAvailable(LoginUI.getUsernameField())) { //no rows (meaning no row with that username) will return false
                //regex found here: https://stackoverflow.com/questions/19605150/regex-for-password-must-contain-at-least-eight-characters-at-least-one-number-a
                if (LoginUI.getPasswordField().matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")) {
                    // generate the salt and store it with the password
                    String salt = genSalt();
                    if (Database.doSqlStatement("INSERT INTO users (username, password, password_salt) " +
                            "VALUES ('" + LoginUI.getUsernameField() + "', '" + hashString(LoginUI.getPasswordField(), salt) + "', '" + salt + "')"
                    )) {
                        LoginUI.feedbackLabel.setText("User added.");
                        return true;
                    }
                } else {
                    LoginUI.feedbackLabel.setText("Password must have a \nminimum of eight characters, \nat least one letter, \none number and \none special character.");
                    return false;
                }
            } else {
                LoginUI.feedbackLabel.setText("Username taken.");
                return false;
            }
        return false;
    }

    static boolean nameAvailable(String username) {
        ResultSet nameConflict = doSqlQuery("SELECT * FROM users where username = '" + username + "'");
        try {
            if (!nameConflict.next()) { //no rows (meaning no row with that username) will return false
                nameConflict.close();
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String hashString(String inputString, String salt) {
        //a hash implementation using SHA-512 and a salt, adapted from https://subscription.packtpub.com/book/security/9781849697767/1/ch01lvl1sec09/creating-a-strong-hash-simple
        //I found these two pages really useful for learning about salting hashes - https://auth0.com/blog/adding-salt-to-hashing-a-better-way-to-store-passwords/ & https://security.stackexchange.com/questions/17421/how-to-store-salt/17435#17435
        //I recognise that this level of encryption is overkill, but I feel that there's little reason not to.

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
