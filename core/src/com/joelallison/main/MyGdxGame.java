package com.joelallison.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import tools.OpenSimplex2S;

import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;

	public static final Vector2 VISIBLE_WORLD_SIZE = new Vector2(16*3, 9*3);
	public static final int TILE_SIZE = 16;
	public float ZOOM = 10f;
	public static final float SCALAR = 2f;

	private OrthographicCamera camera;

	private Texture tree;
	private Sprite treeS;

	private Texture small_tree;
	private Sprite small_treeS;

	private Texture bg;
	private Sprite bgS;

	Random random = new Random();
	long seed = random.nextLong();


	
	@Override
	public void create () {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.zoom = ZOOM;
		camera.setToOrtho(false, SCALAR * VISIBLE_WORLD_SIZE.x * TILE_SIZE, SCALAR * VISIBLE_WORLD_SIZE.y * TILE_SIZE);
		camera.position.set(camera.viewportWidth/2f, camera.viewportHeight/2f, 0);

		camera.update();

		batch = new SpriteBatch();

		tree = new Texture(Gdx.files.internal("tree.png"));
		treeS = new Sprite(tree, 0, 0, 16, 16);

		small_tree = new Texture(Gdx.files.internal("smolTree.png"));
		small_treeS = new Sprite(small_tree, 0, 0, 16, 16);

		bg = new Texture(Gdx.files.internal("bg.png"));
		bgS = new Sprite(bg, 0, 0, 16, 16);
	}

	@Override
	public void render () {
		camera.update();

		batch.setProjectionMatrix(camera.combined);

		float[][] noiseMap = genNoiseMap(seed, VISIBLE_WORLD_SIZE, 4f, 2, 2f, 0.6f); //higher lacunarity for things like trees, lower for grass
		ScreenUtils.clear(0, 0, 0.2f, 1);
		batch.begin();
		for (int x = 0; x < VISIBLE_WORLD_SIZE.x; x++) {
			for (int y = 0; y < VISIBLE_WORLD_SIZE.y; y++) {

				if(noiseMap[x][y] >= 0.9){
					batch.draw(treeS, SCALAR * x*16, SCALAR * y*16, SCALAR * 16, SCALAR * 16);
				}else if(noiseMap[x][y] >= 0.45){ //higher threshold for less midtones, lower for more midtones
					batch.draw(small_treeS, SCALAR * x*16, SCALAR * y*16, SCALAR * 16, SCALAR * 16);
				}else{
					batch.draw(bgS, SCALAR * x*16, SCALAR * y*16, SCALAR * 16, SCALAR * 16);
				}


			}
		}

		batch.end();

		ZOOM--;
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		bg.dispose();
		small_tree.dispose();
		tree.dispose();
	}

	public static float[][] genNoiseMap (long seed, Vector2 Dimensions, float scale, int octaves, float persistence, float lacunarity) {
		OpenSimplex2S noise = new OpenSimplex2S();
		float[][] noiseMap = new float[(int) Dimensions.x][(int) Dimensions.y];

		float minNoiseHeight = Float.MIN_VALUE;
		float maxNoiseHeight = Float.MAX_VALUE;

		for (int y = 0; y < Dimensions.y; y++) {
			for (int x = 0; x < Dimensions.x; x++) {

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

				if (noiseHeight > maxNoiseHeight) {
					maxNoiseHeight = noiseHeight;
				} else if (noiseHeight < minNoiseHeight) {
					minNoiseHeight = noiseHeight;
				}

				noiseMap[x][y] = noiseHeight;
			}
		}

		for (int y = 0; y < Dimensions.y; y++) { //normalises the noise
			for (int x = 0; x < Dimensions.x; x++) {
				noiseMap[x][y] = (float) (inverseLERP(noiseMap[x][y], minNoiseHeight, maxNoiseHeight) * Math.pow(10, 38)); //not sure why it's out by x10^-38 but I fixed it
			}
		}


		return noiseMap;
	}

	static float inverseLERP(float x, float a, float b){
		return (x - a) / (b - a);
	}
}

