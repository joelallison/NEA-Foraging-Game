package com.joelallison.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.joelallison.screens.userInterface.LoginUI;
import com.joelallison.io.Database;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.joelallison.io.Database.doSqlQuery;

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
                byte[] salt = rs.getBytes("password_salt");
                //checking hashed & salted password against stored hashed & salted password
                if (hashString(LoginUI.getPasswordField(), salt).equals(rs.getString("password"))) {
                    LoginUI.feedbackLabel.setText("Login successful.");
                    rs.close();
                    return true;
                } else {
                    //writing 'username or password', when it's clear within the code that the issue is that the username is not in the database, increases security.
                    LoginUI.feedbackLabel.setText("Username or password is incorrect.");
                    rs.close();
                    return false;
                }
            } else {
                //writing 'username or password', when it's clear within the code that the issue is that the username is not in the database, increases security.
                LoginUI.feedbackLabel.setText("Username or password is incorrect.");
                rs.close();
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void register() {
        StringBuilder result = new StringBuilder();
        for (byte aByte : genSalt()) {
            result.append(String.format("%02x", aByte));
        }
        System.out.println(result.toString());
        if (addNewUser()) {
            ((Game) Gdx.app.getApplicationListener()).setScreen(new WorldSelectScreen(LoginUI.getUsernameField()));
        }
    }

    public static boolean addNewUser() {
            if (nameAvailable(LoginUI.getUsernameField())) {
                if (LoginUI.getUsernameField().matches(".{1,20}")) {
                    //password regex found here: https://www.ocpsoft.org/tutorials/regular-expressions/password-regular-expression/
                    if (LoginUI.getPasswordField().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[*.!@$%_?/~+-=]).{8,32}$")) {

                        // generate the salt and store it with the password
                        try {
                        byte[] salt = genSalt();
                        PreparedStatement addUser = Database.doPreparedStatement("INSERT INTO users (username, password, password_salt) " +
                                "VALUES ('" + LoginUI.getUsernameField() + "', '" + hashString(LoginUI.getPasswordField(), salt) + "', ?)");

                            addUser.setBinaryStream(1, new ByteArrayInputStream(salt), salt.length);

                            if (addUser.executeUpdate() > 0) {
                                LoginUI.feedbackLabel.setText("User added.");
                                return true;
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                    } else {
                        LoginUI.feedbackLabel.setText("Password must have a \nminimum of eight characters and maximum of 32, \nat least one letter, one number, \none of these: *.!@$%_?/~+-=, \none uppercase character, one lowercase character.");
                        return false;
                    }
                } else {
                    LoginUI.feedbackLabel.setText("Username must have a length <= 20.");
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
                nameConflict.close();
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String hashString(String inputString, byte[] salt) {
        //a hash implementation using SHA-512 and a salt, adapted from https://subscription.packtpub.com/book/security/9781849697767/1/ch01lvl1sec09/creating-a-strong-hash-simple
        //I found these two pages really useful for learning about salting hashes - https://auth0.com/blog/adding-salt-to-hashing-a-better-way-to-store-passwords/ & https://security.stackexchange.com/questions/17421/how-to-store-salt/17435#17435
        //I recognise that this level of encryption is overkill, but I feel that there's little reason not to.

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt); //loading the salt into the message digest instance
            byte[] byteData = md.digest(inputString.getBytes());
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

    public static byte[] genSalt() {
        //generating a salt to add to the password
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        return (salt);
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
