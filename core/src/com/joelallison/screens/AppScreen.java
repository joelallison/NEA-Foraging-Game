package com.joelallison.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.joelallison.generation.MazeLayer;
import com.joelallison.graphics.Tileset;
import com.joelallison.generation.Layer;
import com.joelallison.user.Creation;
import com.joelallison.user.Database;
import com.joelallison.user.UserInput;
import com.joelallison.generation.TerrainLayer;
import com.joelallison.screens.userInterface.AppInterface;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static com.joelallison.io.JsonHandling.tilesetsJsonToMap;
import static com.joelallison.screens.userInterface.AppInterface.saveProgress;

public class AppScreen implements Screen {
	Stage mainUIStage;
	//viewport displays the generated tiles
	public ExtendViewport viewport;
	public static OrthographicCamera camera;
	SpriteBatch batch;
	ShapeRenderer sr; //for misc UI additions
	public static Creation creation;
	static String username;
	public static final int TILE_SIZE = 32;
	public static final int CHUNK_SIZE = 7;
	public static final Vector2 LEVEL_ASPECT_RATIO = new Vector2(4, 3);
	public static final int LEVEL_ASPECT_SCALAR = 2;
	public static final Vector2 MAP_DIMENSIONS = new Vector2((int) CHUNK_SIZE * LEVEL_ASPECT_SCALAR * LEVEL_ASPECT_RATIO.x, (int) CHUNK_SIZE* LEVEL_ASPECT_SCALAR * LEVEL_ASPECT_RATIO.y);
	int xPos, yPos;
	public static UserInput userInput;
	float stateTime;
	public static HashMap<String, Tileset> tilesets;
	AppInterface userInterface = new AppInterface();
	public AppScreen(Creation creation, String username) {
		AppScreen.creation = creation;
		this.username = username;

		camera = new OrthographicCamera(1920, 1080);
		viewport = new ExtendViewport(1920, 1080, camera);

		batch = new SpriteBatch();
		sr = new ShapeRenderer();

		camera.zoom = 1.2f; //default viewport size
		viewport.apply(true);

		// declare default coords
		xPos = 0;
		yPos = 0;

		//declare player stuff
		userInput = new UserInput(0, 0);

		tilesets = tilesetsJsonToMap("core/src/com/joelallison/graphics/tilesets.json");
		for (Map.Entry<String, Tileset> set : tilesets.entrySet()) {
			set.getValue().initTileset();
		}

		stateTime = 0f;

		mainUIStage = userInterface.genStage(mainUIStage);
		userInterface.genUI(mainUIStage);
	}

	@Override
	public void render(float delta) {
		stateTime += Gdx.graphics.getDeltaTime();
		viewport.apply();
		camera.zoom = MathUtils.clamp(camera.zoom, 0.2f, 4f); //set limits for the size of the viewport
		batch.setProjectionMatrix(viewport.getCamera().combined);
		sr.setProjectionMatrix(viewport.getCamera().combined);
		camera.update();

		userInput.handleInput();
		xPos = userInput.getxPosition();
		yPos = userInput.getyPosition();

		ScreenUtils.clear(creation.getClearColor());

		batch.begin();
		drawLayers();
		batch.end();

		drawUIShapes();

		//update the ui and draw it on top of everything else
		creation.makeLayerNamesUnique();
		userInterface.update(delta);
		mainUIStage.draw();
		mainUIStage.act();
	}

	public void drawUIShapes() {
		sr.begin(ShapeRenderer.ShapeType.Line);

		//draw border around the generated outcome
		sr.setColor(Color.WHITE);
		sr.rect(0f, 0f, MAP_DIMENSIONS.x*TILE_SIZE, MAP_DIMENSIONS.y*TILE_SIZE);

		sr.end();
	}

	public void drawLayers() {
		for (int i = 0; i < creation.layers.size(); i++) {
			((TerrainLayer) creation.layers.get(i)).genValueMap(creation.getLayerSeed(i), MAP_DIMENSIONS, xPos + (int) (creation.layers.get(i).getCenter().x), yPos + (int) (creation.layers.get(i).getCenter().y));
		}

		boolean[][] tileAbove = new boolean[(int) MAP_DIMENSIONS.x][(int) MAP_DIMENSIONS.y];

		for (int x = 0; x < MAP_DIMENSIONS.x; x++) {
			for (int y = 0; y < MAP_DIMENSIONS.y; y++) {
				// top layer to bottom layer
				for (int i = creation.layers.size() - 1; i >= 0; i--) {
					if(creation.layers.get(i).layerShown()) {
						if (tileAbove[x][y] == false) { // no need to draw if there's already a tile above
							switch (getLayerType(creation.layers.get(i))) {
								case "Terrain":
									TextureRegion tile = getTextureForTerrainValue(creation.layers.get(i), x, y);
									if (tile != null) {
										batch.draw(tile, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
										tileAbove[x][y] = true;
									}
									break;
								default:

							}
						}
					}
				}
			}
		}
	}

	public TextureRegion getTextureForTerrainValue(Layer layer, int x, int y) {
		for (int i = 0; i < ((TerrainLayer) layer).tileSpecs.size(); i++) {
			if (((TerrainLayer)layer).valueMap[x][y] > ((TerrainLayer) layer).tileSpecs.get(i).lowerBound) {
				return layer.getTextureFromIndex(i);
			}
		}

		return null; // if there should be no tile drawn at this position for this layer
	}

	public static String getLayerType(Layer layer) {
		return layer.getClass().getName().replace("com.joelallison.generation.","").replace("Layer", "");
	}

	public static void saveCreation() {
		// update creation entry
		// for each layer, update layer
		// 		then get specific layer id
		// 		using that, edit the layer-type specific details
		//		THAT INCLUDES TILE STUFF

		int localLayersCount = creation.layers.size();
		int layersInDatabase = 0;

		boolean done = true;

		ResultSet creationInDatabase = Database.doSqlQuery("SELECT * FROM creation WHERE username = '" + username + "' AND creation_name = '" + creation.name + "';" );
		try {
			if(creationInDatabase.next()) { //if creation is found in database, update it
				Database.doSqlStatement(
						"UPDATE creation " +
								"SET creation_name = '" + creation.name + "', last_accessed_timestamp = '" + Instant.now().toString() + "', creation_seed = " + creation.seed +
								"WHERE username = '" + username + "' AND creation_name = '" + creation.name + "';"
				);
				try {
					//get number of layers in the saved version for later comparison
					ResultSet layerCountRS = Database.doSqlQuery("SELECT COUNT(*) FROM layer WHERE creation_name = '" + creation.name + "' AND username = '" + username + "'");
					if (layerCountRS.next()) {
						layersInDatabase = layerCountRS.getInt("count");
					}
					saveProgress = "Comparing number of layers with database...";
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}else { //creation not found? make a new entry into database
				saveProgress = "Creation not in database, saving as new creation...";
				Database.doSqlStatement("INSERT INTO creation (username, creation_name, created_timestamp, last_accessed_timestamp, creation_seed)" +
						"VALUES ("
						+ "'" + username + "', "
						+ "'" + creation.name + "', "
						+ "'" + creation.dateCreated.toString() + "', "
						+ "'" + Instant.now().toString() + "', "
						+ creation.seed +
						");");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		if (layersInDatabase > localLayersCount) {
			Database.doSqlStatement("DELETE * FROM layer" +
					"WHERE layer_number > " + (localLayersCount - 1));
		}
		for (int i = 0; i < localLayersCount; i++) {
			if (i >= layersInDatabase) {
				if (saveLayer(i, "INSERT INTO")) {
					saveProgress = "Saved layer " + i + "...";
				} else {
					saveProgress = "Error saving layer " + i + "...";
					done = false;
				}
			} else {
				if (saveLayer(i, "UPDATE")) {
					saveProgress = "Saved layer " + i + "...";
				} else {
					saveProgress = "Error saving layer " + i + "...";
					done = false;
				}
			}

		}

		if (done) {
			saveProgress = "Done!";
		}

	}

	static boolean saveLayer(int i, String saveType) {
		System.out.println("SAVE LAYER");
		if (saveType.equals("INSERT INTO")) {
			char layer_type = ' ';
			switch (getLayerType(creation.layers.get(i))){
				case "Terrain":
					layer_type = 'T';
					break;
				case "Maze":
					layer_type = 'M';
					break;
			}

			int layer_id = -1;
			//I found the auto-increment for this particular case (of having what's called a two-way exlusive arc) too complex to implement within PostgreSQL,
			//so I'm generating the layer_id with code
			try {
				ResultSet maxLayerID = Database.doSqlQuery("SELECT MAX(layer_id)+1 FROM layer WHERE layer_type = '" + layer_type + "';");
				maxLayerID.next();
				//gets next biggest layer_id
				layer_id = maxLayerID.getInt("?column?");
				System.out.println(layer_id);
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}

			if (layer_id != -1 && Database.doSqlStatement(saveType + " layer (username, creation_name, layer_number, layer_name, show_layer, layer_type, layer_id, inherit_seed, seed, center_x, center_y, tileset_name)" +
					"VALUES (" +
					"'" + username + "', "
					+ "'" + creation.name + "', "
					+ i + ", "
					+ "'" + creation.layers.get(i).getName() + "', "
					+ Boolean.toString(creation.layers.get(i).layerShown()) + ", "
					+ "'" + layer_type + "', "
					+ layer_id + ", "
					+ creation.layers.get(i).inheritSeed() + ", "
					+ Long.toString(creation.layers.get(i).getSeed()) + ", "
					+ creation.layers.get(i).getCenter().x + ", "
					+ creation.layers.get(i).getCenter().y + ", "
					+ "'" + creation.layers.get(i).tilesetName + "')")) {
				switch (layer_type) {
					case 'T':
						if (Database.doSqlStatement(saveType + " terrain_layer (layer_type, layer_id, scale, octaves, lacunarity, wrap, invert)" +
								"VALUES ("
								+ "'" + layer_type + "', "
								+ layer_id + ", "
								+ ((TerrainLayer) creation.layers.get(i)).getScale() + ", "
								+ ((TerrainLayer) creation.layers.get(i)).getOctaves() + ", "
								+ ((TerrainLayer) creation.layers.get(i)).getLacunarity() + ", "
								+ ((TerrainLayer) creation.layers.get(i)).getWrap() + ", "
								+ ((TerrainLayer) creation.layers.get(i)).isInverted() + ");"))
						{
							for (Tileset.TerrainTileSpec tileSpec: ((TerrainLayer) creation.layers.get(i)).tileSpecs) {
								if (!Database.doSqlStatement(saveType + " terrain_tile_specs (layer_id, tile_name, lower_bound)" +
										"VALUES ("
										+ layer_id + ", "
										+ "'" + tileSpec.name + "', "
										+ tileSpec.lowerBound + ")"
								)) {
									saveProgress = "Unable to save tile " + tileSpec.name + " | " + tileSpec.lowerBound;
									return false; //if a tile isn't able to be saved to the database
								}
							}
							saveProgress = "Layer " + i + " saved.";
							return true;
						}
						break;
					case 'M':
						if (Database.doSqlStatement(saveType + " maze_layer (layer_type, layer_id, width, height)" +
								"VALUES ("
								+ "'" + layer_type + "', "
								+ layer_id + ", "
								+ ((MazeLayer) creation.layers.get(i)).getWidth() + ", "
								+ ((MazeLayer) creation.layers.get(i)).getHeight() + ");"))
						{
							for (Tileset.MazeTileSpec tileSpec: ((MazeLayer) creation.layers.get(i)).tileSpecs) {
								if (!Database.doSqlStatement(saveType + " maze_tile_specs (layer_id, tile_name, lower_bound)" +
										"VALUES ("
										+ layer_id + ", "
										+ "'" + tileSpec.name + "', "
										+ tileSpec.orientationID + ")"
								)) {
									saveProgress = "Unable to save tile " + tileSpec.name + " | " + tileSpec.orientationID;
									return false; //if a tile isn't able to be saved to the database
								}
							}
							saveProgress = "Layer " + i + " saved.";
							return true;
						}
						break;
				}
			}

			return true;
		}else if(saveType.equals("UPDATE")) {
			System.out.println("update");
			//UPDATE layer
			//SET x = y, etc.
			//WHERE layer_number = i, creation_name, username

			//UPDATE layer_type
			//set x = y, etc.
			//WHERE layer_id = from layer

			//UPDATE layer_type_tile_spec
			//SET
			//WHERE layer_id
			return true;
		}

		return false;
	}


	@Override
	public void show() {
		// when the screen is shown
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		camera.position.set(MAP_DIMENSIONS.x * TILE_SIZE / 2, MAP_DIMENSIONS.y * TILE_SIZE / 2, 0);
		viewport.getCamera().update();
	}
	
	@Override
	public void dispose () {
		for (Layer layer: creation.layers) {
			tilesets.get(layer.tilesetName).getSpriteSheet().dispose();
		}
	}
}