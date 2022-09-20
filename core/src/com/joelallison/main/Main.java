package com.joelallison.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.joelallison.entity.Player;
import com.joelallison.level.TileType;


import static com.joelallison.level.Map.*;

import java.util.Random;

public class Main extends ApplicationAdapter {
	private SpriteBatch batch;

	public TileType[] tilesToGen = new TileType[2];

	public static final int TILE_SIZE = 32;
	public static final Vector2 VISIBLE_WORLD_DIMENSIONS = new Vector2(48, 27);

	int x;
	int y;
	public static OrthographicCamera camera;
	private ExtendViewport viewport;

	Player player;

	Random random = new Random();
	long seed = random.nextLong();

	float stateTime;

	@Override
	public void create () {
		x = 0;
		y = 0;

		//declare player stuff
		player = new Player(0, 0, new Texture(Gdx.files.internal("player_tileSheet.png")), 3, 1);
		player.initAnimations();

		//tree generation
		tilesToGen[0] = new TileType("tree", 1, false, 8, 2, 1.55f, 1.1f, -1, true);
		tilesToGen[0].bounds = new float[] {0.38f, 0.4f, 0.6f, 0.7f};
		tilesToGen[0].setSpriteSheet(new Texture(Gdx.files.internal("tree_tileSheet.png")));
		tilesToGen[0].sprites = new TextureRegion[] {new TextureRegion(tilesToGen[0].getSpriteSheet(), 0, 0, 8, 8), //plant
				new TextureRegion(tilesToGen[0].getSpriteSheet(), 8, 0, 8, 8), //bush
				new TextureRegion(tilesToGen[0].getSpriteSheet(), 16, 0, 8, 8), //dark green tree
				new TextureRegion(tilesToGen[0].getSpriteSheet(), 24, 0, 8, 8)}; //light green tree

		//rocks generation
		tilesToGen[1] = new TileType("rock", 2, true, 1, 2, 1.3f, 6f, 2, true);
		tilesToGen[1].bounds = new float[] {0.985f, 0.99f};
		tilesToGen[1].setSpriteSheet(new Texture(Gdx.files.internal("rock_tileSheet.png")));
		tilesToGen[1].sprites = new TextureRegion[] {new TextureRegion(tilesToGen[1].getSpriteSheet(), 0, 0, 8, 8), //small rock
				new TextureRegion(tilesToGen[1].getSpriteSheet(), 8, 0, 8, 8)}; //big rock


		//String[][] level = Level.compositeLevel(seed, VISIBLE_WORLD_DIMENSIONS, x, y);

		batch = new SpriteBatch();

		//stage = new Stage();
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(VISIBLE_WORLD_DIMENSIONS.x * TILE_SIZE, VISIBLE_WORLD_DIMENSIONS.y * TILE_SIZE, camera);
		viewport.getCamera().position.set(VISIBLE_WORLD_DIMENSIONS.x * TILE_SIZE / 2, VISIBLE_WORLD_DIMENSIONS.y * TILE_SIZE / 2, 0);
		camera.zoom = 0.5f; //default
		stateTime = 0f;
	}

	@Override
	public void render () {
		ScreenUtils.clear(0.1215686f, 0.09411765f, 0.07843137f, 1);
		stateTime += Gdx.graphics.getDeltaTime();

		player.handleInput();
		viewport.apply();
		camera.zoom = MathUtils.clamp(camera.zoom, 0.2f, 1f);
		batch.setProjectionMatrix(viewport.getCamera().combined);
		camera.update();
		x = player.getxPos();
		y = player.getyPos();


		float[][] noiseMap0 = genNoiseMap(seed, VISIBLE_WORLD_DIMENSIONS, x, y, tilesToGen[0].getScaleVal(), tilesToGen[0].getOctavesVal(), tilesToGen[0].getPersistenceVal(), tilesToGen[0].getLacunarityVal(), tilesToGen[0].getWrapVal(), tilesToGen[0].doInvert());
		float[][] noiseMap1 = genNoiseMap(seed, VISIBLE_WORLD_DIMENSIONS, x, y, tilesToGen[1].getScaleVal(), tilesToGen[1].getOctavesVal(), tilesToGen[1].getPersistenceVal(), tilesToGen[1].getLacunarityVal(), tilesToGen[1].getWrapVal(), tilesToGen[1].doInvert());

		batch.begin();

		for (int x = 0; x < VISIBLE_WORLD_DIMENSIONS.x; x++) {
			for (int y = 0; y < VISIBLE_WORLD_DIMENSIONS.y; y++) {

				for (int i = 0; i < tilesToGen[0].sprites.length; i++) {
					if (noiseMap0[x][y] >= tilesToGen[0].bounds[i]){
						batch.draw(tilesToGen[0].sprites[i], x* TILE_SIZE, y* TILE_SIZE, TILE_SIZE, TILE_SIZE);
					}
				}
			}
		}

		for (int x = 0; x < VISIBLE_WORLD_DIMENSIONS.x; x++) {
			for (int y = 0; y < VISIBLE_WORLD_DIMENSIONS.y; y++) {

				for (int i = 0; i < tilesToGen[1].sprites.length; i++) {
					if (noiseMap1[x][y] >= tilesToGen[1].bounds[i]){
						batch.draw(tilesToGen[1].sprites[i], x* TILE_SIZE, y* TILE_SIZE, TILE_SIZE, TILE_SIZE);
					}
				}
			}
		}

		//batch.draw(Player.getFrame(stateTime, true), camera.viewportWidth / 2, camera.viewportHeight / 2, 16, 16);

		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
	
	@Override
	public void dispose () {
		batch.dispose();

		for (TileType x:tilesToGen) {
			x.getSpriteSheet().dispose();
		}
	}
}