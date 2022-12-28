package com.joelallison.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class Init extends Game {

    SpriteBatch batch;
    Stage UIStage;
    public ExtendViewport viewport;
    public OrthographicCamera camera;

    public Color defaultBackgroundColor = new Color(0.1215686f, 0.09411765f, 0.07843137f, 1);

    public void create() {
        camera = new OrthographicCamera(1920, 1080);
        viewport = new ExtendViewport(1920, 1080, camera);

        batch = new SpriteBatch();
        UIStage = new Stage();
        this.setScreen(new StartScreen(this));
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
    }

}
