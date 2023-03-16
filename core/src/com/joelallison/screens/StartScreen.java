package com.joelallison.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class StartScreen extends Game {

    SpriteBatch batch;
    Stage stage;
    public ExtendViewport viewport;
    public OrthographicCamera camera;

    public void create() {
        camera = new OrthographicCamera(1920, 1080);
        viewport = new ExtendViewport(1920, 1080, camera);

        batch = new SpriteBatch();
        stage = new Stage();

        this.setScreen(new LoginScreen());
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
    }

}
