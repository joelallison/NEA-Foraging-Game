package com.joelallison;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.joelallison.main.Main;

import static com.joelallison.main.Main.TILE_SIZE;
import static com.joelallison.main.Main.ASPECT_RATIO;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode((int) (ASPECT_RATIO.x*TILE_SIZE), (int) (ASPECT_RATIO.y*TILE_SIZE));
		config.useVsync(true);
		config.setForegroundFPS(60);
		config.setTitle("Forest Game");
		config.setWindowIcon("assets/tree.png");
		new Lwjgl3Application(new Main(), config);
		/*

		for (int[] row:printMaze) {
			for (int i:row) {
				System.out.print(i);
			}
			System.out.println();
		}*/
	}


}
