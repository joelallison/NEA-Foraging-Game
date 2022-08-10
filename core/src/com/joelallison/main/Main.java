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
	public static final int TILE_SIZE = 16;
	public static float ZOOM = 0.3f; //default value
	public static final float SCALAR = 8*7;
	public static final Vector2 ASPECT_RATIO = new Vector2(7, 4);
	public static final Vector2 VISIBLE_WORLD_SIZE = new Vector2(ASPECT_RATIO.x, ASPECT_RATIO.y).scl(SCALAR); //7div4 = 1.75, making this a 1.75:1 aspect ratio. 16div9 = 1.77, meaning that this is very close to standard HDTV aspect.

	int x;
	int y;
	public static OrthographicCamera camera;

	Player player;

	Random random = new Random();
	long seed = random.nextLong();

	@Override
	public void create () {
		x = 0;
		y = 0;

		player = new Player(0, 0);

		tilesToGen[0] = new TileType("tree", 1, 2, 2, 1.7f, 0.7f, -1, false);
		tilesToGen[0].bounds = new float[] {0, 0.5f, 0.6f, 0.75f};
		tilesToGen[0].setSpriteSheet(new Texture(Gdx.files.internal("tree_tileSheet.png")));
		tilesToGen[0].sprites = new TextureRegion[] {new TextureRegion(tilesToGen[0].getSpriteSheet(), 0, 0, 8, 8),
				new TextureRegion(tilesToGen[0].getSpriteSheet(), 8, 0, 8, 8),
				new TextureRegion(tilesToGen[0].getSpriteSheet(), 16, 0, 8, 8),
				new TextureRegion(tilesToGen[0].getSpriteSheet(), 24, 0, 8, 8)};


		batch = new SpriteBatch();
		sr = new ShapeRenderer();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCALAR * VISIBLE_WORLD_SIZE.x * TILE_SIZE, SCALAR * VISIBLE_WORLD_SIZE.y * TILE_SIZE);
		camera.position.set(camera.viewportWidth/2f, camera.viewportHeight/2f, 0);
		camera.update();
	}

	@Override
	public void render () {
		player.handleMovement();
		x = player.getxPos();
		y = player.getyPos();

		float[][] noiseMap = genNoiseMap(seed, VISIBLE_WORLD_SIZE, x, y, tilesToGen[0].getScaleVal(), tilesToGen[0].getOctavesVal(), tilesToGen[0].getPersistenceVal(), tilesToGen[0].getLacunarityVal(), tilesToGen[0].getWrapVal(), tilesToGen[0].doInvert());
		ScreenUtils.clear(0, 0.1f, 0.1f, 1);
		batch.begin();

		for (int x = 0; x < VISIBLE_WORLD_SIZE.x; x++) {
			for (int y = 0; y < VISIBLE_WORLD_SIZE.y; y++) {

				batch.draw(tilesToGen[0].sprites[0], x* TILE_SIZE, y* TILE_SIZE, TILE_SIZE, TILE_SIZE);
				for (int i = 1; i < tilesToGen[0].sprites.length; i++) {
					if (noiseMap[x][y] >= tilesToGen[0].bounds[i]){
						batch.draw(tilesToGen[0].sprites[i], x* TILE_SIZE, y* TILE_SIZE, TILE_SIZE, TILE_SIZE);
					}
				}
			}
		}

		batch.end();

		player.zoom();
		camera.zoom = MathUtils.clamp(camera.zoom, 0.2f, 0.4f);
		sr.setProjectionMatrix(camera.combined);
		camera.update();
	}
	
	@Override
	public void dispose () {
		sr.dispose();
	}
}