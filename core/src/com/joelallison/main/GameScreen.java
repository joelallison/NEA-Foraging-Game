package com.joelallison.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.joelallison.entity.Player;
import com.joelallison.level.Tile;

import static com.joelallison.level.Map.*;

import java.util.Random;

public class GameScreen implements Screen {
	final Init system;

	public Tile[] tilesToGen = new Tile[2];

	public static final int TILE_SIZE = 32;
	public static final int CHUNK_SIZE = 8;
	public static final Vector2 ASPECT_RATIO = new Vector2(4, 3);
	public static final Vector2 VISIBLE_WORLD_DIMENSIONS = new Vector2(CHUNK_SIZE*ASPECT_RATIO.x, CHUNK_SIZE*ASPECT_RATIO.y);

	int x;
	int y;

	Player player;

	Random random = new Random();
	long seed = random.nextLong();

	float stateTime;


	public GameScreen(final Init system) {
		this.system = system;

		x = 0;
		y = 0;

		//declare player stuff
		player = new Player(new Vector2(0, 0), new Texture(Gdx.files.internal("player_tileSheet.png")), 3, 1);
		player.initAnimations();

		generateTiles();

		system.batch = new SpriteBatch();

		//camera and viewport setup
		system.viewport = new ExtendViewport(VISIBLE_WORLD_DIMENSIONS.x * TILE_SIZE, VISIBLE_WORLD_DIMENSIONS.y * TILE_SIZE, system.camera);
		system.viewport.getCamera().position.set(VISIBLE_WORLD_DIMENSIONS.x * TILE_SIZE / 2, VISIBLE_WORLD_DIMENSIONS.y * TILE_SIZE / 2, 0);
		system.camera.zoom = 0.5f; //this is the default value

		stateTime = 0f;
	}

	public void generateTiles() {
		//tree generation
		tilesToGen[0] = new Tile("tree", 1, false, 8, 2, 1.55f, 1.1f, -1, true);
		tilesToGen[0].bounds = new float[] {0.38f, 0.4f, 0.6f, 0.7f};
		tilesToGen[0].setSpriteSheet(new Texture(Gdx.files.internal("tree_tileSheet.png")));
		tilesToGen[0].sprites = new TextureRegion[] {new TextureRegion(tilesToGen[0].getSpriteSheet(), 0, 0, 8, 8), //plant
				new TextureRegion(tilesToGen[0].getSpriteSheet(), 8, 0, 8, 8), //bush
				new TextureRegion(tilesToGen[0].getSpriteSheet(), 16, 0, 8, 8), //dark green tree
				new TextureRegion(tilesToGen[0].getSpriteSheet(), 24, 0, 8, 8)}; //light green tree

		//rocks generation
		tilesToGen[1] = new Tile("rock", 2, true, 1, 2, 1.3f, 6f, 2, true);
		tilesToGen[1].bounds = new float[] {0.985f, 0.99f};
		tilesToGen[1].setSpriteSheet(new Texture(Gdx.files.internal("rock_tileSheet.png")));
		tilesToGen[1].sprites = new TextureRegion[] {new TextureRegion(tilesToGen[1].getSpriteSheet(), 0, 0, 8, 8), //small rock
				new TextureRegion(tilesToGen[1].getSpriteSheet(), 8, 0, 8, 8)}; //big rock


	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0.1215686f, 0.09411765f, 0.07843137f, 1);
		stateTime += Gdx.graphics.getDeltaTime();

		player.handleInput();
		system.viewport.apply();
		system.camera.zoom = MathUtils.clamp(system.camera.zoom, 0.2f, 1f);
		system.batch.setProjectionMatrix(system.viewport.getCamera().combined);
		system.camera.update();
		x = (int) player.getPosition().x;
		y = (int) player.getPosition().y;

		Vector2 chunksToLoad = goingToNewChunk(player);

		float[][] noiseMap0 = genNoiseMap(seed, VISIBLE_WORLD_DIMENSIONS, x, y, tilesToGen[0].getScaleVal(), tilesToGen[0].getOctavesVal(), tilesToGen[0].getPersistenceVal(), tilesToGen[0].getLacunarityVal(), tilesToGen[0].getWrapVal(), tilesToGen[0].doInvert());
		//float[][] noiseMap1 = genNoiseMap(seed, VISIBLE_WORLD_DIMENSIONS, x, y, tilesToGen[1].getScaleVal(), tilesToGen[1].getOctavesVal(), tilesToGen[1].getPersistenceVal(), tilesToGen[1].getLacunarityVal(), tilesToGen[1].getWrapVal(), tilesToGen[1].doInvert());

		system.batch.begin();

		for (int x = 0; x < VISIBLE_WORLD_DIMENSIONS.x; x++) {
			for (int y = 0; y < VISIBLE_WORLD_DIMENSIONS.y; y++) {

				for (int i = 0; i < tilesToGen[0].sprites.length; i++) {
					if (noiseMap0[x][y] >= tilesToGen[0].bounds[i]){
						system.batch.draw(tilesToGen[0].sprites[i], x* TILE_SIZE, y* TILE_SIZE, TILE_SIZE, TILE_SIZE);
					}
				}
			}
		}

		/*for (int x = 0; x < VISIBLE_WORLD_DIMENSIONS.x; x++) {
			for (int y = 0; y < VISIBLE_WORLD_DIMENSIONS.y; y++) {

				for (int i = 0; i < tilesToGen[1].sprites.length; i++) {
					if (noiseMap1[x][y] >= tilesToGen[1].bounds[i]){
						system.batch.draw(tilesToGen[1].sprites[i], x* TILE_SIZE, y* TILE_SIZE, TILE_SIZE, TILE_SIZE);
					}
				}
			}
		}*/


		system.batch.end();
	}


	//using Vector2 to describe the direction of the chunk that the player is about to go into (relative to the player)
	public Vector2 goingToNewChunk(Player player) {
		Vector2 chunkDirection = new Vector2(0, 0);

		System.out.println(Math.abs(player.getPosition().x) + " --> " + (Math.abs(player.getPosition().x % CHUNK_SIZE)) +  " --> " + (Math.floor(Math.abs(player.getPosition().x / CHUNK_SIZE))) * CHUNK_SIZE + "... " + Math.signum(player.getPosition().x));

		//finds out if player is halfway or more through their current chunk, and then declares chunk[s] to get as chunks in the direction of travel.
		if (Math.abs(player.getPosition().x % CHUNK_SIZE) >=  CHUNK_SIZE / 2){
			//(Math.floor(Math.abs(player.getPosition().x / CHUNK_SIZE))) * CHUNK_SIZE)
			// chunkDirection.x = Math.signum(
			System.out.println("halfway through chunk x");
		}
		if (Math.abs(player.getPosition().y % CHUNK_SIZE) >= (Math.floor(Math.abs(player.getPosition().y / CHUNK_SIZE))) * CHUNK_SIZE){
			chunkDirection.y = Math.signum(player.getPosition().y);
		}

		return chunkDirection;
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
	}
	
	@Override
	public void dispose () {
		for (Tile x:tilesToGen) {
			x.getSpriteSheet().dispose();
		}
	}
}