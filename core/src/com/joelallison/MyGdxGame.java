package com.joelallison;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import tools.OpenSimplex2S;

import java.util.Random;

import static java.lang.Math.abs;

public class MyGdxGame extends ApplicationAdapter {
	ShapeRenderer sr;

	static final int SCREEN_WIDTH = 256;
	static final int SCREEN_HEIGHT = 256;
	static final int noiseSize = 32;

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

		Random random = new Random();

		long seed = random.nextLong();
		float[][] grid = genNoiseMap(1L, noiseSize, 3f, 2, 6f, 0.3f); //4f, 16, 6f, 0.9f for
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

	public static float[][] genNoiseMap (long seed, int size, float scale, int octaves, float persistence, float lacunarity) {
		OpenSimplex2S noise = new OpenSimplex2S();
		float[][] noiseMap = new float[size][size];

		float minNoiseHeight = Float.MIN_VALUE;
		float maxNoiseHeight = Float.MAX_VALUE;

		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {

				float amplitude = 1;
				float frequency = 1;
				float noiseHeight = 0;

				for (int i = 0; i < octaves; i++) {

					float sampleX = x / scale * frequency;
					float sampleY = y / scale * frequency;

					float noiseValue = (noise.noise2_ImproveX(seed, sampleX, sampleY));
					noiseHeight = noiseValue * amplitude;

					amplitude *= persistence;
					frequency *= lacunarity;
				}

				noiseMap[x][y] = noiseHeight;
			}
		}
		return noiseMap;
	}

	static float inverseLERP(float x, float a, float b){
		return (x - a) / (b - a);
	}
}
