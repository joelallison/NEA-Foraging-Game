package com.joelallison.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.utils.ScreenUtils;


public class MainMenuScreen implements Screen {

    final Init system;

    public MainMenuScreen(final Init system) {
        this.system = system;
        system.UIStage = UserInterface.generateUIStage(system.batch, "login");
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

        system.UIStage.act();
        system.UIStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        system.UIStage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose () {
    }
}
