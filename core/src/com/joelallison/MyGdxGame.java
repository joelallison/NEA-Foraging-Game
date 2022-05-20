package com.joelallison;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import tools.OpenSimplex2S;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

import static java.lang.Math.abs;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch sb;

	static final int noiseSize = 128;

	private OrthographicCamera cam;

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

		cam = new OrthographicCamera(noiseSize*16, noiseSize*16 * (h / w));

		cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
		cam.update();

		sb = new SpriteBatch();

		tree = new Texture(Gdx.files.internal("tree.png"));
		treeS = new Sprite(tree, 0, 0, 16, 16);

		small_tree = new Texture(Gdx.files.internal("smolTree.png"));
		small_treeS = new Sprite(small_tree, 0, 0, 16, 16);

		bg = new Texture(Gdx.files.internal("bg.png"));
		bgS = new Sprite(bg, 0, 0, 16, 16);

	}

	@Override
	public void render () {
		cam.update();

		sb.setProjectionMatrix(cam.combined);

		float[][] noiseMap = genNoiseMap(seed, noiseSize, 4f, 2, 2f, 0.6f); //higher lacunarity for things like trees, lower for grass
		ScreenUtils.clear(1, 1, 1, 1);
		sb.begin();
		for (int x = 0; x < noiseSize; x++) {
			for (int y = 0; y < noiseSize; y++) {

				if(noiseMap[x][y] >= 0.9){
					sb.draw(treeS, x*64, y*64, 64, 64);
				}else if(noiseMap[x][y] >= 0.45){ //higher threshold for less midtones, lower for more midtones
					sb.draw(small_treeS, x*64, y*64, 64, 64);
				}else{
					sb.draw(bgS, x*64, y*64, 64, 64);
				}


			}
		}
		sb.end();
	}
	
	@Override
	public void dispose () {
		sb.dispose();
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

				if (noiseHeight > maxNoiseHeight) {
					maxNoiseHeight = noiseHeight;
				} else if (noiseHeight < minNoiseHeight) {
					minNoiseHeight = noiseHeight;
				}

				noiseMap[x][y] = noiseHeight;
			}
		}

		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				noiseMap[x][y] = (float) (inverseLERP(noiseMap[x][y], minNoiseHeight, maxNoiseHeight) * Math.pow(10, 38));
			}
		}


		return noiseMap;
	}

	static float inverseLERP(float x, float a, float b){
		return (x - a) / (b - a);
	}
}

