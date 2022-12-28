package com.joelallison.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.google.gson.Gson;
import com.joelallison.display.Tileset;
import com.joelallison.user.Player;
import com.joelallison.generation.FileHandling;
import com.joelallison.generation.TerrainGenSetting;
import com.joelallison.generation.TerrainGenSetting.TerrainLayer;
import com.joelallison.screens.UserInterface.MainInterface;

import static com.joelallison.generation.TerrainGen.*;

public class MainScreen implements Screen {
	final Init system;

	ExtendViewport levelViewport;
	OrthographicCamera levelCamera;

	public TerrainGenSetting terrainGen = new TerrainGenSetting("terrain", 3L, 2);

	public static final int TILE_SIZE = 32;
	public static final int CHUNK_SIZE = 7;
	public static final Vector2 LEVEL_ASPECT_RATIO = new Vector2(4, 3);
	public static final int LEVEL_ASPECT_SCALAR = 2;
	public static final Vector2 MAP_DIMENSIONS = new Vector2(CHUNK_SIZE * LEVEL_ASPECT_SCALAR * LEVEL_ASPECT_RATIO.x, CHUNK_SIZE* LEVEL_ASPECT_SCALAR * LEVEL_ASPECT_RATIO.y);

	public static final Vector2 WINDOW_ASPECT_RATIO = new Vector2(16, 9);

	int xPos, yPos;

	Player player;

	float stateTime;

	Gson gson = new Gson();
	Tileset[] tilesets;
	MainInterface userInterface = new MainInterface();
	public MainScreen(final Init system) {
		this.system = system;
		system.camera.zoom = 0.5f;
		system.viewport.apply(true);

		levelCamera = new OrthographicCamera(MAP_DIMENSIONS.x, MAP_DIMENSIONS.y);
		levelViewport = new ExtendViewport(MAP_DIMENSIONS.x*TILE_SIZE, MAP_DIMENSIONS.y*TILE_SIZE, levelCamera);

		xPos = 0;
		yPos = 0;

		//declare player stuff
		player = new Player(0, 0, system);

		tilesets = gson.fromJson(FileHandling.readJSONTileData("core/src/com/joelallison/display/tilesets.json"), Tileset[].class);
		for (Tileset t : tilesets) { t.initTexture(); }

		system.batch = new SpriteBatch();

		stateTime = 0f;

		system.UIStage = userInterface.genStage();
		userInterface.genUI();
	}

	public void generateTiles() {
		terrainGen.layers[0] = new TerrainLayer("tree", Float.parseFloat(userInterface.getValues()[0]), Integer.parseInt(userInterface.getValues()[1]), Float.parseFloat(userInterface.getValues()[2]), Integer.parseInt(userInterface.getValues()[3]), Boolean.parseBoolean(userInterface.getValues()[4]));
		terrainGen.layers[0].tileset = tilesets[0];
		terrainGen.layers[0].tileset.setColor(new Color(0.1215686f, 0.09411765f, 0.07843137f, 1));
		terrainGen.layers[0].tileBounds = new Tileset.TileBound[] {
				new Tileset.TileBound("plant", 0.35f),
				new Tileset.TileBound("bush", 0.4f),
				new Tileset.TileBound("tree_1", 0.6f),
				new Tileset.TileBound("tree_2", 0.7f)
		};
	}

	@Override
	public void render(float delta) {
		stateTime += Gdx.graphics.getDeltaTime();

		system.viewport.apply();
		system.camera.zoom = MathUtils.clamp(system.camera.zoom, 0.2f, 4f);
		system.batch.setProjectionMatrix(system.viewport.getCamera().combined);
		system.camera.update();

		player.handleInput();
		xPos = player.getxPosition();
		yPos = player.getyPosition();

		generateTiles();
		ScreenUtils.clear(terrainGen.layers[0].tileset.getColor());

		float[][] noiseMap0 = genTerrain(terrainGen.getSeed(), MAP_DIMENSIONS, xPos, yPos, terrainGen.layers[0].getScaleVal(), terrainGen.layers[0].getOctavesVal(), terrainGen.layers[0].getLacunarityVal(), terrainGen.layers[0].getWrapVal(), terrainGen.layers[0].doInvert());

		//clipping mask for rendering
		//Rectangle scissors = new Rectangle();
		//Rectangle clipBounds = new Rectangle(0, 0, 320, 320);

		system.batch.begin();

		for (int x = 0; x < MAP_DIMENSIONS.x; x++) {
			for (int y = 0; y < MAP_DIMENSIONS.y; y++) {
				for (int i = 0; i < terrainGen.layers[0].tileBounds.length; i++) {
					if (noiseMap0[x][y] >= terrainGen.layers[0].tileBounds[i].lowerBound) {
						system.batch.draw(terrainGen.layers[0].getTextureFromIndex(i), x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
					}
				}
			}
		}

		system.batch.end();

		userInterface.update();
		system.UIStage.draw();
		system.UIStage.act();

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
			x.tileset.getSpriteSheet().dispose();
		}
	}
}