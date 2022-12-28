package com.joelallison.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.joelallison.screens.UserInterface.MenuInterface;

public class MenuScreen implements Screen {

    final Init system;

    float stateTime;
    MenuInterface userInterface = new MenuInterface();
    public MenuScreen(final Init system) {
        this.system = system;
        system.batch = new SpriteBatch();

        system.UIStage = userInterface.genStage();
        userInterface.genUI();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.SPACE) {
                    system.setScreen(new MainScreen(system));
                    dispose();
                }
                return true;
            }
        });
    }

    public void render(float delta) {
        //stateTime += Gdx.graphics.getDeltaTime();

        system.viewport.apply();
        system.batch.setProjectionMatrix(system.viewport.getCamera().combined);
        system.camera.update();
        ScreenUtils.clear(system.defaultBackgroundColor);

        //userInterface.update();



        system.batch.begin();

        system.batch.end();

        system.UIStage.act();
        system.UIStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        system.viewport.update(width, height);
        system.camera.position.set(system.camera.viewportWidth / 2, system.camera.viewportHeight / 2, 0);
        system.camera.update();
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
