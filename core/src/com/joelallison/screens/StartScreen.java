package com.joelallison.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import static com.joelallison.screens.UserInterface.UserInterface.chosenSkin;


public class StartScreen implements Screen {

    final Init system;

    public StartScreen(final Init system) {
        this.system = system;

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.SPACE) {
                    system.setScreen(new MenuScreen(system));
                    dispose();
                }
                return true;
            }
        });


    }

    public void render(float delta) {
        system.UIStage.act();
        system.UIStage.draw();

    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose () {
    }
}
