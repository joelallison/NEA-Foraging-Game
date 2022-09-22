package com.joelallison;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.joelallison.main.Main;

import static com.joelallison.main.Main.TILE_SIZE;
import static com.joelallison.main.Main.VISIBLE_WORLD_DIMENSIONS;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode((int) (VISIBLE_WORLD_DIMENSIONS.x*TILE_SIZE), (int) (VISIBLE_WORLD_DIMENSIONS.y*TILE_SIZE));
		config.useVsync(true);
		config.setForegroundFPS(60);
		config.setTitle("World Gen Tool");
		config.setWindowIcon("assets/tree.png");
		new Lwjgl3Application(new Main(), config);
	}


}
