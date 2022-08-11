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
import com.badlogic.gdx.utils.ScreenUtils;
import com.joelallison.entity.Player;
import com.joelallison.level.TileType;

import java.util.ArrayList;
import java.util.*;
import java.util.Random;
import java.util.regex.*;

import static com.joelallison.level.Map.*;

public class Main extends ApplicationAdapter {
	private SpriteBatch batch;

	public TileType[] tilesToGen = new TileType[2];

	public ArrayList<String[][]> mapsToCombine;
	public static final int TILE_SIZE = 16;
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

		//tree generation
		tilesToGen[0] = new TileType("tree", 1, false, 8, 2, 1.55f, 1.1f, -1, true);
		tilesToGen[0].bounds = new float[] {0.38f, 0.4f, 0.6f, 0.7f};
		tilesToGen[0].setSpriteSheet(new Texture(Gdx.files.internal("tree_tileSheet.png")));
		tilesToGen[0].sprites = new TextureRegion[] {new TextureRegion(tilesToGen[0].getSpriteSheet(), 0, 0, 8, 8), //plant
				new TextureRegion(tilesToGen[0].getSpriteSheet(), 8, 0, 8, 8), //bush
				new TextureRegion(tilesToGen[0].getSpriteSheet(), 16, 0, 8, 8), //dark green tree
				new TextureRegion(tilesToGen[0].getSpriteSheet(), 24, 0, 8, 8)}; //light green tree

		//rocks generation
		tilesToGen[1] = new TileType("rock", 2, true, 1, 2, 1.3f, 6f, 2, true);
		tilesToGen[1].bounds = new float[] {0.95f, 0.995f};
		tilesToGen[1].setSpriteSheet(new Texture(Gdx.files.internal("rock_tileSheet.png")));
		tilesToGen[1].sprites = new TextureRegion[] {new TextureRegion(tilesToGen[1].getSpriteSheet(), 0, 0, 8, 8), //small rock
				new TextureRegion(tilesToGen[1].getSpriteSheet(), 8, 0, 8, 8)}; //big rock


		batch = new SpriteBatch();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCALAR * VISIBLE_WORLD_SIZE.x * TILE_SIZE, SCALAR * VISIBLE_WORLD_SIZE.y * TILE_SIZE);
		camera.position.set(camera.viewportWidth/2f, camera.viewportHeight/2f, 0);
		camera.zoom = 0.15f;
		camera.update();
	}

	@Override
	public void render () {
		handleInput();
		camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 0.4f);
		batch.setProjectionMatrix(camera.combined);
		camera.update();
		x = player.getxPos();
		y = player.getyPos();

		ScreenUtils.clear(0.1215686f, 0.09411765f, 0.07843137f, 1);



		TileType[] sortedTilesToGen = sortByPriority(tilesToGen);
		String[][] terrain = constructTerrain(x, y, seed, sortByPriority(tilesToGen));


		batch.begin();

		for (int x = 0; x < terrain.length; x++) {
			for (int y = 0; y < terrain[x].length; y++) {
				if(terrain[x][y] != "-"){
					String[] tile = terrain[x][y].split("(?<=\\d)(?=\\D)|(?<=\\D)(?=\\d)");
					batch.draw(sortedTilesToGen[Integer.parseInt(tile[0])].sprites[Integer.parseInt(tile[2])], x* TILE_SIZE*SCALAR, y* TILE_SIZE*SCALAR, TILE_SIZE*SCALAR, TILE_SIZE*SCALAR);
				}
			}
		}

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