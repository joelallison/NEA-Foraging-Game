package com.joelallison.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class Init extends Game {

    SpriteBatch batch;
    Stage UIStage;

    public static OrthographicCamera camera;
    ExtendViewport viewport;

    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        UIStage = new Stage();
        this.setScreen(new MainMenuScreen(this));
        dispose();
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
    }

}
