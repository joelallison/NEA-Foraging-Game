package com.joelallison;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.joelallison.level.Maze;
import com.joelallison.main.Main;

import static com.joelallison.main.Main.TILE_SIZE;
import static com.joelallison.main.Main.VISIBLE_WORLD_SIZE;

public class DesktopLauncher {
	public static void main (String[] arg) {
		/*Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode((int) (VISIBLE_WORLD_SIZE.x * TILE_SIZE), (int) (VISIBLE_WORLD_SIZE.y * TILE_SIZE));
		config.useVsync(true);
		config.setForegroundFPS(60);
		config.setTitle("Forest Game");
		config.setWindowIcon("assets/tree.png");
		new Lwjgl3Application(new Main(), config);*/
		Maze maze = new Maze(42069, 255,255);

		int[][] printMaze = maze.genMaze();

		for (int[] row:printMaze) {
			for (int i:row) {
				System.out.print(i);
			}
			System.out.println();
		}
	}


}
