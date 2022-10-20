package com.joelallison.screens;

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
import com.joelallison.level.Layer;

import static com.joelallison.level.Map.*;

import java.util.Random;

public class GameScreen implements Screen {
	final Init system;

	public Layer[] tileLayers = new Layer[2];

	public static final int TILE_SIZE = 32;
	public static final int CHUNK_SIZE = 8;

	//padding values are in tiles
	public static final int X_PADDING = 4;
	public static final int Y_PADDING = 4;


	public static final Vector2 ASPECT_RATIO = new Vector2(4, 3);
	public static final Vector2 VISIBLE_WORLD_DIMENSIONS = new Vector2(CHUNK_SIZE*ASPECT_RATIO.x, CHUNK_SIZE*ASPECT_RATIO.y);

	int xPos, yPos;

	Player player;

	Random random = new Random();
	long seed = random.nextLong();

	float stateTime;


	public GameScreen(final Init system) {
		this.system = system;

		xPos = 0;
		yPos = 0;

		//declare player stuff
		player = new Player(0, 0, new Texture(Gdx.files.internal("player_tileSheet.png")), 3, 1);
		player.initAnimations();

		generateTiles();

		system.batch = new SpriteBatch();

		//camera and viewport setup
		system.viewport = new ExtendViewport(VISIBLE_WORLD_DIMENSIONS.x * TILE_SIZE, VISIBLE_WORLD_DIMENSIONS.y * TILE_SIZE, system.camera);
		system.viewport.getCamera().position.set(VISIBLE_WORLD_DIMENSIONS.x * TILE_SIZE / 2, VISIBLE_WORLD_DIMENSIONS.y * TILE_SIZE / 2, 0);
		system.camera.zoom = 0.5f; //this is the default value

		stateTime = 0f;

		system.UIStage = UserInterface.generateUIStage(system.batch, "game");
	}

	public void generateTiles() {
		//tree generation
		tileLayers[0] = new Layer("tree", 1, false, 8, 2, 1.55f, 1.1f, -1, true);
		tileLayers[0].bounds = new float[] {0.38f, 0.4f, 0.6f, 0.7f};
		tileLayers[0].setSpriteSheet(new Texture(Gdx.files.internal("tree_tileSheet.png")));
		tileLayers[0].sprites = new TextureRegion[] {new TextureRegion(tileLayers[0].getSpriteSheet(), 0, 0, 8, 8), //plant
				new TextureRegion(tileLayers[0].getSpriteSheet(), 8, 0, 8, 8), //bush
				new TextureRegion(tileLayers[0].getSpriteSheet(), 16, 0, 8, 8), //dark green tree
				new TextureRegion(tileLayers[0].getSpriteSheet(), 24, 0, 8, 8)}; //light green tree

		//rocks generation
		tileLayers[1] = new Layer("rock", 2, true, 1, 2, 1.3f, 6f, 2, true);
		tileLayers[1].bounds = new float[] {0.985f, 0.99f};
		tileLayers[1].setSpriteSheet(new Texture(Gdx.files.internal("rock_tileSheet.png")));
		tileLayers[1].sprites = new TextureRegion[] {new TextureRegion(tileLayers[1].getSpriteSheet(), 0, 0, 8, 8), //small rock
				new TextureRegion(tileLayers[1].getSpriteSheet(), 8, 0, 8, 8)}; //big rock


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
		xPos = player.getxPosition();
		yPos = player.getyPosition();

		float[][] noiseMap0 = genNoiseMap(seed, VISIBLE_WORLD_DIMENSIONS, xPos, yPos, tileLayers[0].getScaleVal(), tileLayers[0].getOctavesVal(), tileLayers[0].getPersistenceVal(), tileLayers[0].getLacunarityVal(), tileLayers[0].getWrapVal(), tileLayers[0].doInvert());
		//float[][] noiseMap1 = genNoiseMap(seed, VISIBLE_WORLD_DIMENSIONS, x, y, tilesToGen[1].getScaleVal(), tilesToGen[1].getOctavesVal(), tilesToGen[1].getPersistenceVal(), tilesToGen[1].getLacunarityVal(), tilesToGen[1].getWrapVal(), tilesToGen[1].doInvert());

		system.batch.begin();

		for (int x = 0 + (int) (VISIBLE_WORLD_DIMENSIONS.x * 0.1); x < VISIBLE_WORLD_DIMENSIONS.x - (int) (VISIBLE_WORLD_DIMENSIONS.x * 0.1); x++) {
			for (int y = 0 + (int) (VISIBLE_WORLD_DIMENSIONS.y * 0.1); y < VISIBLE_WORLD_DIMENSIONS.y - (int) (VISIBLE_WORLD_DIMENSIONS.y * 0.1); y++) {

				for (int i = 0; i < tileLayers[0].sprites.length; i++) {
					if (noiseMap0[x][y] >= tileLayers[0].bounds[i]) {
						system.batch.draw(tileLayers[0].sprites[i], x* TILE_SIZE, y* TILE_SIZE, TILE_SIZE, TILE_SIZE);
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

		system.UIStage.act();
		system.UIStage.draw();
	}


	//using Vector2 to describe the direction of the chunk that the player is about to go into (relative to the player)
	/*public Vector2 goingToNewChunk(Player player) {
		Vector2 chunkDirection = new Vector2(0, 0);



		//finds out if player is halfway or more through their current chunk, and then declares chunk[s] to get as chunks in the direction of travel.
		if (Math.abs(player.getxPosition() % CHUNK_SIZE) >=  CHUNK_SIZE / 2){
			//(Math.floor(Math.abs(player.getPosition().x / CHUNK_SIZE))) * CHUNK_SIZE)
			chunkDirection.x = Math.signum(player.getxPosition() - customRound(player.getxPosition(),CHUNK_SIZE));
			System.out.println(player.getxPosition() + " - " + (customRound(player.getxPosition() - CHUNK_SIZE / 2,CHUNK_SIZE) + CHUNK_SIZE / 2) + " = " + (player.getxPosition() - (customRound(player.getxPosition()	 - CHUNK_SIZE / 2,CHUNK_SIZE) + CHUNK_SIZE / 2)));
		}
		if (Math.abs(player.getyPosition() % CHUNK_SIZE) >= (Math.floor(Math.abs(player.getyPosition() / CHUNK_SIZE))) * CHUNK_SIZE){
			chunkDirection.y = Math.signum(player.getyPosition());
		}

		return chunkDirection;
	}*/

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
		system.camera.position.set(VISIBLE_WORLD_DIMENSIONS.x * TILE_SIZE / 2, VISIBLE_WORLD_DIMENSIONS.y * TILE_SIZE / 2, 0);
		system.camera.update();

	}
	
	@Override
	public void dispose () {
		for (Layer x: tileLayers) {
			x.getSpriteSheet().dispose();
		}
	}
}