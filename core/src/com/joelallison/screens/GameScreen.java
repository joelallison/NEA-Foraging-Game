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
import com.joelallison.display.Tileset;
import com.joelallison.entity.Player;
import com.joelallison.generation.TerrainGenSetting;
import com.joelallison.generation.TerrainGenSetting.TerrainLayer;

import static com.joelallison.generation.TerrainGen.*;

import java.util.Random;

public class GameScreen implements Screen {
	final Init system;

	ExtendViewport levelViewport;
	OrthographicCamera levelCamera;

	public TerrainGenSetting terrainGen = new TerrainGenSetting("terrain", 0L, 2);

	public static final int TILE_SIZE = 32;
	public static final int CHUNK_SIZE = 7;
	public static final Vector2 LEVEL_ASPECT_RATIO = new Vector2(4, 3);
	public static final int LEVEL_ASPECT_SCALAR = 2;
	public static final Vector2 MAP_DIMENSIONS = new Vector2(CHUNK_SIZE * LEVEL_ASPECT_SCALAR * LEVEL_ASPECT_RATIO.x, CHUNK_SIZE* LEVEL_ASPECT_SCALAR * LEVEL_ASPECT_RATIO.y);

	public static final Vector2 WINDOW_ASPECT_RATIO = new Vector2(16, 9);

	int xPos, yPos;

	Player player;

	Random random = new Random();
	long seed = random.nextLong();

	float stateTime;

	Tileset[] tilesets = new Tileset[3];


	public GameScreen(final Init system) {
		this.system = system;
		system.camera.zoom = 0.5f;
		system.viewport.apply(true);

		levelCamera = new OrthographicCamera(MAP_DIMENSIONS.x, MAP_DIMENSIONS.y);
		levelViewport = new ExtendViewport(MAP_DIMENSIONS.x*TILE_SIZE, MAP_DIMENSIONS.y*TILE_SIZE, levelCamera);

		xPos = 0;
		yPos = 0;

		//declare player stuff
		player = new Player(0, 0, new Texture(Gdx.files.internal("tree_tileSheet.png")), 3, 1, system);
		player.initAnimations();

		tilesets[0] = new Tileset("Trees & Rocks", new Texture(Gdx.files.internal("tree_tileSheet.png")), 8);
		tilesets[0].map.put("ground", new TextureRegion(tilesets[0].getSpriteSheet(), 0, 0, tilesets[0].tileSize, tilesets[0].tileSize));
		tilesets[0].map.put("plant", new TextureRegion(tilesets[0].getSpriteSheet(), 8, 0, tilesets[0].tileSize, tilesets[0].tileSize));
		tilesets[0].map.put("bush", new TextureRegion(tilesets[0].getSpriteSheet(), 16, 0, tilesets[0].tileSize, tilesets[0].tileSize));
		tilesets[0].map.put("tree_1", new TextureRegion(tilesets[0].getSpriteSheet(), 24, 0, tilesets[0].tileSize, tilesets[0].tileSize));
		tilesets[0].map.put("tree_2", new TextureRegion(tilesets[0].getSpriteSheet(), 32, 0, tilesets[0].tileSize, tilesets[0].tileSize));
		tilesets[0].map.put("rock_1", new TextureRegion(tilesets[0].getSpriteSheet(), 40, 0, tilesets[0].tileSize, tilesets[0].tileSize));
		tilesets[0].map.put("rock_2", new TextureRegion(tilesets[0].getSpriteSheet(), 48, 0, tilesets[0].tileSize, tilesets[0].tileSize));

		tilesets[1] = new Tileset("Kenney Micro Roguelike COLOUR", new Texture(Gdx.files.internal("tree_tileSheet.png")), 8);

		//tilesets[2]





		generateTiles();

		system.batch = new SpriteBatch();

		stateTime = 0f;

		system.UIStage = UserInterface.generateUIStage(system.batch, "game");
	}

	public void generateTiles() {
		//tree generation
		terrainGen.layers[0] = new TerrainLayer("tree", 1f, 2, 2f, 0.4f, 2, false);
		terrainGen.layers[0].bounds = new float[] {0.38f, 0.4f, 0.6f, 0.7f};
		terrainGen.layers[0].setSpriteSheet(new Texture(Gdx.files.internal("tree_tileSheet.png")));
		terrainGen.layers[0].sprites = new TextureRegion[] {new TextureRegion(terrainGen.layers[0].getSpriteSheet(), 0, 0, 8, 8), //plant
				new TextureRegion(terrainGen.layers[0].getSpriteSheet(), 8, 0, 8, 8), //bush
				new TextureRegion(terrainGen.layers[0].getSpriteSheet(), 16, 0, 8, 8), //dark green tree
				new TextureRegion(terrainGen.layers[0].getSpriteSheet(), 24, 0, 8, 8)}; //light green tree

		//rocks generation
		/*terrainGen.layers[1] = new TerrainLayer("rock", 1, 2, 1.3f, 6f, 2, true);
		terrainGen.layers[1].bounds = new float[] {0.985f, 0.99f};
		terrainGen.layers[1].setSpriteSheet(new Texture(Gdx.files.internal("rock_tileSheet.png")));
		terrainGen.layers[1].sprites = new TextureRegion[] {new TextureRegion(terrainGen.layers[1].getSpriteSheet(), 0, 0, 8, 8), //small rock
				new TextureRegion(terrainGen.layers[1].getSpriteSheet(), 8, 0, 8, 8)}; //big rock */

	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0.1215686f, 0.09411765f, 0.07843137f, 1);
		stateTime += Gdx.graphics.getDeltaTime();

		system.viewport.apply();
		system.camera.zoom = MathUtils.clamp(system.camera.zoom, 0.2f, 4f);
		system.batch.setProjectionMatrix(system.viewport.getCamera().combined);
		system.camera.update();

		player.handleInput();
		xPos = player.getxPosition();
		yPos = player.getyPosition();

		float[][] noiseMap0 = genTerrain(seed, MAP_DIMENSIONS, xPos, yPos, terrainGen.layers[0].getScaleVal(), terrainGen.layers[0].getOctavesVal(), terrainGen.layers[0].getPersistenceVal(), terrainGen.layers[0].getLacunarityVal(), terrainGen.layers[0].getWrapVal(), terrainGen.layers[0].doInvert());

		//clipping mask for rendering
		//Rectangle scissors = new Rectangle();
		//Rectangle clipBounds = new Rectangle(0, 0, 320, 320);

		system.batch.begin();

			for (int x = 0; x < MAP_DIMENSIONS.x; x++) {
				for (int y = 0; y < MAP_DIMENSIONS.y; y++) {

					for (int i = 0; i < terrainGen.layers[0].sprites.length; i++) {
						if (noiseMap0[x][y] >= terrainGen.layers[0].bounds[i]) {
							system.batch.draw(terrainGen.layers[0].sprites[i], x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
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
		for (TerrainLayer x: terrainGen.layers) {
			x.getSpriteSheet().dispose();
		}
	}
}