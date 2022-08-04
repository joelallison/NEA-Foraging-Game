package com.joelallison.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import entity.Player;
import level.Map;
import tools.OpenSimplex2S;

import java.util.Random;

import static level.Map.genNoiseMap;

public class MyGdxGame extends ApplicationAdapter {
	private ShapeRenderer sr;

	public static final int TILE_SIZE = 16;
	public static final float ZOOM = 1.2f; //After testing, I think that 0.2f should be the max zoomed in, 2f should be the max zoomed out and 0.8f should be default.
	public static final float SCALAR = 8f;
	public static final Vector2 VISIBLE_WORLD_SIZE = new Vector2(7*7, 4*7).scl(SCALAR); //7div4 = 1.75, making this a 1.75:1 aspect ratio. 16div9 = 1.77, meaning that this is very close to standard HDTV aspect.

	int x;
	int y;
	private OrthographicCamera camera;

	Player player;

	Random random = new Random();
	long seed = random.nextLong();

	@Override
	public void create () {
		x = 0;
		y = 0;

		player = new Player(0, 0);

		sr = new ShapeRenderer();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCALAR * VISIBLE_WORLD_SIZE.x * TILE_SIZE, SCALAR * VISIBLE_WORLD_SIZE.y * TILE_SIZE);
		camera.position.set(camera.viewportWidth/2f, camera.viewportHeight/2f, 0);

		camera.update();
	}

	@Override
	public void render () {
		camera.zoom = ZOOM;
		camera.update();

		sr.setProjectionMatrix(camera.combined);

		player.handleMovement();
		x = player.getxPos();
		y = player.getyPos();

		float[][] noiseMap = genNoiseMap(seed, VISIBLE_WORLD_SIZE, x, y, 4f, 2, 2f, 0.6f, 2, true);
		String[][] map = processMap(noiseMap, 0);
		ScreenUtils.clear(0, 0.1f, 0.1f, 1);
		sr.begin(ShapeRenderer.ShapeType.Filled);


		for (int x = 0; x < VISIBLE_WORLD_SIZE.x; x++) {
			for (int y = 0; y < VISIBLE_WORLD_SIZE.y; y++) {

				if(map[x][y] == "x"){
					sr.setColor(0.5f, 0.5f, 0.3f, 1f);
				}else if(map[x][y] == "y") {
					sr.setColor(0.3f, 0.3f, 0.2f, 1);
				}else{
					sr.setColor(0, 0.1f, 0.1f, 1);
				}
				sr.rect(SCALAR * x*16, SCALAR * y*16, SCALAR * 16, SCALAR * 16);


			}
		}

		sr.end();



	}
	
	@Override
	public void dispose () {
		sr.dispose();
	}

	public static String[][] processMap (float[][] map, int mapType) { //convert floats to discrete tiles
		String[][] outputMap = new String[map.length][map[0].length];

		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[x].length; y++) {
				if(Map.wrapValue(map[x][y], 2, false) >= 0.61) {
					outputMap[x][y] = "x";
				} else if(map[x][y] >= 0.6) {
					outputMap[x][y] = "y";
				} else {
					outputMap[x][y] = "-";
				}
			}
		}

		return outputMap;
	}


}