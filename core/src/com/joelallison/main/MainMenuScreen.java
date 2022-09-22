package com.joelallison.main;

import com.badlogic.gdx.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.joelallison.level.TileType;

public class MainMenuScreen implements Screen {

    final Init system;

    public MainMenuScreen(final Init system) {
        this.system = system;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.SPACE) {
                    system.setScreen(new GameScreen(system));
                    dispose();
                }
                return true;
            }
        });
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1215686f, 0.09411765f, 0.07843137f, 1);

        system.batch.begin();
        system.font.draw(system.batch, "HELLO", 100, 150);
        system.font.draw(system.batch, "[press space]", 100, 100);
        system.batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void dispose () {
    }
}
