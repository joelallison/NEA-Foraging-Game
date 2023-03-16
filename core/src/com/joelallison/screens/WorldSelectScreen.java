package com.joelallison.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.joelallison.screens.userInterface.WorldUI;
import com.joelallison.generation.World;

import static com.joelallison.io.FileHandling.importTilesets;
import static com.joelallison.screens.AppScreen.tilesets;

public class WorldSelectScreen implements Screen {
    SpriteBatch batch;
    Stage menuUIStage;
    ExtendViewport viewport;
    OrthographicCamera camera;
    float stateTime;
    WorldUI userInterface = new WorldUI();
    public static String username;

    public WorldSelectScreen(String username) {
        //this is done here as it needs to be done before AppScreen yet StartScreen doesn't stick around long enough to guarantee it gets run
        tilesets = importTilesets("core/src/com/joelallison/tilesets/");

        camera = new OrthographicCamera(1920, 1080);
        viewport = new ExtendViewport(1920, 1080, camera);

        batch = new SpriteBatch();

        this.username = username;
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
        ScreenUtils.clear(new Color(0.365f, 0.525f, 0.310f, 1f));

        batch.begin();
        batch.end();

        menuUIStage.act(stateTime);
        menuUIStage.draw();
    }

    public static void loadWorldIntoApp(World world, String username) {
        ((Game) Gdx.app.getApplicationListener()).setScreen(new AppScreen(world, username));
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

