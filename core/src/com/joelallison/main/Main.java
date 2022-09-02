package com.joelallison.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.joelallison.entity.Player;
import com.joelallison.level.Level;

import java.util.Random;

public class Main extends ApplicationAdapter {
	private SpriteBatch batch;

	public static final Vector2 MAZE_DIMENSIONS = new Vector2(129,129);
	public static final int SQUARE_TILES_PER_MAZE_CELL = 16;
	public static final Vector2 WORLD_SIZE = MAZE_DIMENSIONS.scl(SQUARE_TILES_PER_MAZE_CELL);
	public static final int TILE_SIZE = 16;
	public static final Vector2 ASPECT_RATIO = new Vector2(80, 45);

	int x;
	int y;

	int[][] outputMaze;

	private Stage stage;
	private OrthographicCamera camera;

	Player player;

	Random random = new Random();
	long seed = random.nextLong();

	@Override
	public void create () {
		x = 0;
		y = 0;

		player = new Player(0, 0);

		String[][] level = Level.compositeLevel();

		batch = new SpriteBatch();

		stage = new Stage();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, ASPECT_RATIO.x * TILE_SIZE, ASPECT_RATIO.y * TILE_SIZE);
		camera.position.set(camera.viewportWidth/2f, camera.viewportHeight/2f, 0);
		camera.zoom = 0.6f; //default
		camera.update();
	}

	@Override
	public void render () {
		handleInput();
		camera.zoom = MathUtils.clamp(camera.zoom, 0.4f, 1f);
		batch.setProjectionMatrix(camera.combined);
		camera.update();
		x = player.getxPos();
		y = player.getyPos();

		ScreenUtils.clear(0.1215686f, 0.09411765f, 0.07843137f, 1);


		//float[][] noiseMap0 = genNoiseMap(seed, ASPECT_RATIO, x, y, tilesToGen[0].getScaleVal(), tilesToGen[0].getOctavesVal(), tilesToGen[0].getPersistenceVal(), tilesToGen[0].getLacunarityVal(), tilesToGen[0].getWrapVal(), tilesToGen[0].doInvert());
		//float[][] noiseMap1 = genNoiseMap(seed, ASPECT_RATIO, x, y, tilesToGen[1].getScaleVal(), tilesToGen[1].getOctavesVal(), tilesToGen[1].getPersistenceVal(), tilesToGen[1].getLacunarityVal(), tilesToGen[1].getWrapVal(), tilesToGen[1].doInvert());

		batch.begin();

		/*
		for (int x = 0; x < ASPECT_RATIO.x; x++) {
			for (int y = 0; y < ASPECT_RATIO.y; y++) {

				for (int i = 0; i < tilesToGen[0].sprites.length; i++) {
					if (noiseMap0[x][y] >= tilesToGen[0].bounds[i]){
						batch.draw(tilesToGen[0].sprites[i], x* TILE_SIZE, y* TILE_SIZE, TILE_SIZE, TILE_SIZE);
					}
				}
			}
		}

		for (int x = 0; x < ASPECT_RATIO.x; x++) {
			for (int y = 0; y < ASPECT_RATIO.y; y++) {

				for (int i = 0; i < tilesToGen[1].sprites.length; i++) {
					if (noiseMap1[x][y] >= tilesToGen[1].bounds[i]){
						batch.draw(tilesToGen[1].sprites[i], x* TILE_SIZE, y* TILE_SIZE, TILE_SIZE, TILE_SIZE);
					}
				}
			}
		}*/

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	public void handleInput(){

		//movement
		if(Gdx.input.isKeyPressed(Input.Keys.W)){
			player.setyPos(player.getyPos() + 1);
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.S)){
			player.setyPos(player.getyPos() - 1);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.A)){
			player.setxPos(player.getxPos() - 1);
		}
		else if(Gdx.input.isKeyPressed(Input.Keys.D)){
			player.setxPos(player.getxPos() + 1);
		}

		//zoom
		if(Gdx.input.isKeyPressed(Input.Keys.P)){
			camera.zoom += 0.02;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.O)){
			camera.zoom -= 0.02;
		}
	}
}