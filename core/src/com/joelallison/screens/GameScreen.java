package com.joelallison.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.joelallison.entity.Player;
import com.joelallison.generation.TerrainGenSetting.TerrainLayer;

import static com.joelallison.generation.TerrainGen.*;

import java.util.Random;

public class GameScreen implements Screen {
	final Init system;

	ExtendViewport levelViewport;
	OrthographicCamera levelCamera;

	public TerrainLayer[] tileLayers = new TerrainLayer[2];

	public static final int TILE_SIZE = 32;
	public static final int CHUNK_SIZE = 7;

	public static final Vector2 ASPECT_RATIO = new Vector2(4, 3);
	public static final Vector2 MAP_DIMENSIONS = new Vector2(CHUNK_SIZE*ASPECT_RATIO.x, CHUNK_SIZE*ASPECT_RATIO.y);

	int xPos, yPos;

	Player player;

	Random random = new Random();
	long seed = random.nextLong();

	float stateTime;


	public GameScreen(final Init system) {
		this.system = system;
		system.camera.zoom = 1f;
		system.viewport.apply(true);

		levelCamera = new OrthographicCamera(MAP_DIMENSIONS.x, MAP_DIMENSIONS.y);
		levelViewport = new ExtendViewport(MAP_DIMENSIONS.x*TILE_SIZE, MAP_DIMENSIONS.y*TILE_SIZE, levelCamera);

		xPos = 0;
		yPos = 0;

		//declare player stuff
		player = new Player(0, 0, new Texture(Gdx.files.internal("player_tileSheet.png")), 3, 1, system);
		player.initAnimations();

		generateTiles();

		system.batch = new SpriteBatch();

		stateTime = 0f;

		system.UIStage = UserInterface.generateUIStage(system.batch, "game");
	}

	public void generateTiles() {
		//tree generation
		tileLayers[0] = new TerrainLayer("tree", 1, false, 8, 2, 1.55f, 1.1f, -1, true);
		tileLayers[0].bounds = new float[] {0.38f, 0.4f, 0.6f, 0.7f};
		tileLayers[0].setSpriteSheet(new Texture(Gdx.files.internal("tree_tileSheet.png")));
		tileLayers[0].sprites = new TextureRegion[] {new TextureRegion(tileLayers[0].getSpriteSheet(), 0, 0, 8, 8), //plant
				new TextureRegion(tileLayers[0].getSpriteSheet(), 8, 0, 8, 8), //bush
				new TextureRegion(tileLayers[0].getSpriteSheet(), 16, 0, 8, 8), //dark green tree
				new TextureRegion(tileLayers[0].getSpriteSheet(), 24, 0, 8, 8)}; //light green tree

		//rocks generation
		tileLayers[1] = new TerrainLayer("rock", 2, true, 1, 2, 1.3f, 6f, 2, true);
		tileLayers[1].bounds = new float[] {0.985f, 0.99f};
		tileLayers[1].setSpriteSheet(new Texture(Gdx.files.internal("rock_tileSheet.png")));
		tileLayers[1].sprites = new TextureRegion[] {new TextureRegion(tileLayers[1].getSpriteSheet(), 0, 0, 8, 8), //small rock
				new TextureRegion(tileLayers[1].getSpriteSheet(), 8, 0, 8, 8)}; //big rock


	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0.1215686f, 0.09411765f, 0.07843137f, 1);
		stateTime += Gdx.graphics.getDeltaTime();

		system.viewport.apply();
		system.camera.zoom = MathUtils.clamp(system.camera.zoom, 0.2f, 1f);
		system.batch.setProjectionMatrix(system.viewport.getCamera().combined);
		system.camera.update();

		player.handleInput();
		xPos = player.getxPosition();
		yPos = player.getyPosition();

		float[][] noiseMap0 = genTerrain(seed, MAP_DIMENSIONS, xPos, yPos, tileLayers[0].getScaleVal(), tileLayers[0].getOctavesVal(), tileLayers[0].getPersistenceVal(), tileLayers[0].getLacunarityVal(), tileLayers[0].getWrapVal(), tileLayers[0].doInvert());

		//clipping mask for rendering
		//Rectangle scissors = new Rectangle();
		//Rectangle clipBounds = new Rectangle(0, 0, 320, 320);

		system.batch.begin();

			for (int x = 0; x < MAP_DIMENSIONS.x; x++) {
				for (int y = 0; y < MAP_DIMENSIONS.y; y++) {

					for (int i = 0; i < tileLayers[0].sprites.length; i++) {
						if (noiseMap0[x][y] >= tileLayers[0].bounds[i]) {
							system.batch.draw(tileLayers[0].sprites[i], x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
						}
					}

				}
			}

		system.batch.end();

		system.UIStage.act();
		system.UIStage.draw();
	}

	private int customRound(float input, int multiple) {
		return multiple * Math.round(input/multiple);
	}



	@Override
	public void show() {
		// when the screen is shown
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
	public void resize(int width, int height) {
		system.viewport.update(width, height);
		system.camera.position.set(MAP_DIMENSIONS.x * TILE_SIZE / 2, MAP_DIMENSIONS.y * TILE_SIZE / 2, 0);
		system.viewport.getCamera().update();

	}
	
	@Override
	public void dispose () {
		for (TerrainLayer x: tileLayers) {
			x.getSpriteSheet().dispose();
		}
	}
}