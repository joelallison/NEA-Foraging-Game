package com.joelallison;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.joelallison.main.MyGdxGame;

import static com.joelallison.main.MyGdxGame.TILE_SIZE;
import static com.joelallison.main.MyGdxGame.VISIBLE_WORLD_SIZE;
import static com.joelallison.main.MyGdxGame.SCALAR;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode((int) (VISIBLE_WORLD_SIZE.x * TILE_SIZE), (int) (VISIBLE_WORLD_SIZE.y * TILE_SIZE));
		config.useVsync(true);
		config.setForegroundFPS(60);
		config.setTitle("Forest Game");
		config.setWindowIcon("assets/tree.png");
		new Lwjgl3Application(new MyGdxGame(), config);
	}


}
