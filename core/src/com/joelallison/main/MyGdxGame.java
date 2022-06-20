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
import tools.OpenSimplex2S;

import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	private ShapeRenderer sr;


	public static final int TILE_SIZE = 16;
	public static final float ZOOM = 2f; //After testing, I think that 0.2f should be the max zoomed in, 2f should be the max zoomed out and 0.8f should be default.
	public static final float SCALAR = 2f;
	public static final Vector2 VISIBLE_WORLD_SIZE = new Vector2(7*7, 4*7).scl(SCALAR); //7div4 = 1.75, making this a 1.75:1 aspect ratio. 16div9 = 1.77, meaning that this is very close to standard HDTV aspect.


	private OrthographicCamera camera;

	Random random = new Random();
	long seed = random.nextLong();

	@Override
	public void create () {
		sr = new ShapeRenderer();

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

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


		float[][] noiseMap = genNoiseMap(seed, VISIBLE_WORLD_SIZE, 0, 0, 4f, 2, 1f, 1f, -1, true);
		ScreenUtils.clear(0, 0.1f, 0.1f, 1);
		sr.begin(ShapeRenderer.ShapeType.Filled);
		for (int x = 0; x < VISIBLE_WORLD_SIZE.x; x++) {
			for (int y = 0; y < VISIBLE_WORLD_SIZE.y; y++) {

				sr.setColor(noiseMap[x][y], noiseMap[x][y], noiseMap[x][y], 1);
				sr.rect(SCALAR * x*16, SCALAR * y*16, SCALAR * 16, SCALAR * 16);


			}
		}

		sr.end();

	}
	
	@Override
	public void dispose () {
		sr.dispose();
	}

	public static float[][] genNoiseMap (long seed, Vector2 Dimensions, float xOffset, float yOffset, float scale, int octaves, float persistence, float lacunarity, int wrapFactor, boolean invertWrap) {
		//greater scale zooms in, halved scale from normal could be used for map data. I think scale of 4 is best for most stuff
		//higher octaves simplifies the noise, 2 is the best option in almost all cases
		//higher persistence makes the values tend towards higher values
		//lower lacunarity is smoother looking - higher lacunarity for things like trees, lower for grass

		//if you double the scale but double the lacunarity, the output is basically  the same. if you double the scale and halve the lacunarity, it's like you quadrupled the scale.

		OpenSimplex2S noise = new OpenSimplex2S();
		float[][] noiseMap = new float[(int) Dimensions.x][(int) Dimensions.y];

		float minNoiseHeight = Float.MIN_VALUE;
		float maxNoiseHeight = Float.MAX_VALUE;

		for (int y = (int) yOffset; y < Dimensions.y + yOffset; y++) {
			for (int x = (int) xOffset; x < Dimensions.x + yOffset; x++) {

				float amplitude = 1;
				float frequency = 1;
				float noiseHeight = 0;

				for (int i = 0; i < octaves; i++) {

					float sampleX = (x-(Dimensions.x/2)) / scale * frequency;
					float sampleY = (y-(Dimensions.y/2)) / scale * frequency;

					float noiseValue = (noise.noise2_ImproveX(seed, sampleX, sampleY));
					noiseHeight = noiseValue * amplitude;

					amplitude *= persistence;
					frequency *= lacunarity;
				}

				if (noiseHeight > maxNoiseHeight) {
					maxNoiseHeight = noiseHeight;
				} else if (noiseHeight < minNoiseHeight) {
					minNoiseHeight = noiseHeight;
				}

				noiseMap[x-(int)xOffset][y-(int)yOffset] = noiseHeight;
			}
		}

		if(wrapFactor != -1){
			for (int y = 0; y < Dimensions.y; y++) { //normalises the noise
				for (int x = 0; x < Dimensions.x; x++) {
					noiseMap[x][y] = wrapValue((float) (inverseLERP(noiseMap[x][y], minNoiseHeight, maxNoiseHeight) * Math.pow(10, 38)), wrapFactor, invertWrap); //not sure why it's out by x10^-38 but I fixed it
				}
			}
		}else{
			for (int y = 0; y < Dimensions.y; y++) { //normalises the noise
				for (int x = 0; x < Dimensions.x; x++) {
					noiseMap[x][y] = (float) (inverseLERP(noiseMap[x][y], minNoiseHeight, maxNoiseHeight) * Math.pow(10, 38)); //not sure why it's out by x10^-38 but I fixed it
				}
			}
		}






		return noiseMap;
	}

	static float inverseLERP(float x, float a, float b){
		return (x - a) / (b - a);
	}

	static float wrapValue(float input, int factor, boolean invert){
		if(invert){
			return ((Math.abs(((input * factor) - factor/2)))*-1)+1;
		}else{
			return Math.abs(((input * factor) - factor/2));
		}

	}
}

