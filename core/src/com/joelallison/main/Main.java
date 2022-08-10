package com.joelallison.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.joelallison.entity.Player;
import com.joelallison.level.TileType;

import java.util.Random;

import static com.joelallison.level.Map.genNoiseMap;

public class Main extends ApplicationAdapter {
	private ShapeRenderer sr;
	private SpriteBatch batch;

	public TileType[] tilesToGen = new TileType[1];
	public static final int TILE_SIZE = 4;
	public static float ZOOM = 1.2f; //default value
	public static final float SCALAR = 8*7;
	public static final Vector2 ASPECT_RATIO = new Vector2(7, 4);
	public static final Vector2 VISIBLE_WORLD_SIZE = new Vector2(ASPECT_RATIO.x, ASPECT_RATIO.y).scl(SCALAR); //7div4 = 1.75, making this a 1.75:1 aspect ratio. 16div9 = 1.77, meaning that this is very close to standard HDTV aspect.

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

		tilesToGen[0] = new TileType("tree", 1, 4, 2, 1.8f, 1f, -1, false);
		tilesToGen[0].bounds = new float[] {0.8f, 0.7f, 0.65f};
		tilesToGen[0].setSpriteSheet(new Texture(Gdx.files.internal("tree_tileSheet.png")));
		tilesToGen[0].sprites = new TextureRegion[] {new TextureRegion(tilesToGen[0].getSpriteSheet(), 0, 0, 8, 8),
				new TextureRegion(tilesToGen[0].getSpriteSheet(), 8, 0, 8, 8),
				new TextureRegion(tilesToGen[0].getSpriteSheet(), 16, 0, 8, 8)};


		batch = new SpriteBatch();
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
		ZOOM = ZOOM + player.zoom();
		ZOOM = MathUtils.clamp(ZOOM, 0.2f, 0.4f);
		sr.setProjectionMatrix(camera.combined);

		player.handleMovement();
		x = player.getxPos();
		y = player.getyPos();

		System.out.println(ZOOM);



		float[][] noiseMap = genNoiseMap(seed, VISIBLE_WORLD_SIZE, x, y, tilesToGen[0].getScaleVal(), tilesToGen[0].getOctavesVal(), tilesToGen[0].getPersistenceVal(), tilesToGen[0].getLacunarityVal(), tilesToGen[0].getWrapVal(), tilesToGen[0].doInvert());
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
				sr.rect(SCALAR * x* TILE_SIZE, SCALAR * y* TILE_SIZE, SCALAR * TILE_SIZE, SCALAR * TILE_SIZE);


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
				if(map[x][y] >= 0.5) {
					outputMap[x][y] = "y";
				} else {
					outputMap[x][y] = "-";
				}
			}
		}

		return outputMap;
	}


}