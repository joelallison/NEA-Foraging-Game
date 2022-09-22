package com.joelallison.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class Init extends Game {

    SpriteBatch batch;
    public BitmapFont font;

    public static OrthographicCamera camera;
    ExtendViewport viewport;

    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(); // use libGDX's default Arial font
        camera = new OrthographicCamera();
        this.setScreen(new MainMenuScreen(this));
        dispose();
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }

}
