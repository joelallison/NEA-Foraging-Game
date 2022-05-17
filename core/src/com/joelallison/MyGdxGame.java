package com.joelallison;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import tools.OpenSimplex2S;
import static java.lang.Math.abs;

public class MyGdxGame extends ApplicationAdapter {
	ShapeRenderer sr;

	static final int SCREEN_WIDTH = 256;
	static final int SCREEN_HEIGHT = 256;
	static final int noiseSize = 16;

	private OrthographicCamera cam;


	
	@Override
	public void create () {

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		cam = new OrthographicCamera(noiseSize, noiseSize * (h / w));

		cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
		cam.update();

		sr = new ShapeRenderer();

	}

	@Override
	public void render () {
		cam.update();

		sr.setProjectionMatrix(cam.combined);

		float[][] grid = noiseMap(1L, noiseSize, 0.8f);
		ScreenUtils.clear(1, 1, 1, 1);
		sr.begin(ShapeRenderer.ShapeType.Filled);
		for (int x = 0; x < noiseSize; x++) {
			for (int y = 0; y < noiseSize; y++) {
				sr.setColor(grid[x][y],grid[x][y],grid[x][y],1);

				sr.rect(x, y, 1, 1);

			}
		}
		sr.end();
	}
	
	@Override
	public void dispose () {
		sr.dispose();
	}

	public static float[][] noiseMap (long seed, int size, float scale, int octaves, float persistence, float lacunarity) {
		OpenSimplex2S noise = new OpenSimplex2S();
		float[][] grid = new float[size][size];

		

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				grid[x][y] = (float) (abs(noise.noise2(seed, x, y)) / scale);
			}
		}

		return grid;
	}
}
