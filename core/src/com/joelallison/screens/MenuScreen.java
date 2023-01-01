package com.joelallison.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.joelallison.screens.UserInterface.MenuInterface;
import com.joelallison.screens.UserInterface.UserInterface;

public class MenuScreen implements Screen {

    SpriteBatch batch;
    Stage menuUIStage;
    ExtendViewport viewport;
    OrthographicCamera camera;

    float stateTime;
    MenuInterface userInterface = new MenuInterface();
    public MenuScreen() {
        camera = new OrthographicCamera(1920, 1080);
        viewport = new ExtendViewport(1920, 1080, camera);

        batch = new SpriteBatch();

        menuUIStage = userInterface.genStage(menuUIStage);
        userInterface.genUI(menuUIStage);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.ENTER) {
                    ((Game)Gdx.app.getApplicationListener()).setScreen(new MainScreen());
                    dispose();
                }
                return true;
            }
        });
    }

    public void render(float delta) {
        //stateTime += Gdx.graphics.getDeltaTime();

        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        camera.update();
        ScreenUtils.clear(UserInterface.defaultBackgroundColor);

        //userInterface.update();



        batch.begin();

        batch.end();

        menuUIStage.act();
        menuUIStage.draw();
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
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose () {
    }
}
