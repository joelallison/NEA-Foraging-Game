package com.joelallison;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.joelallison.level.FileHandling;
import com.joelallison.level.Maze;
import com.joelallison.main.Init;

import java.util.Arrays;

import static com.joelallison.main.GameScreen.TILE_SIZE;
import static com.joelallison.main.GameScreen.VISIBLE_WORLD_DIMENSIONS;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode((int) (VISIBLE_WORLD_DIMENSIONS.x*TILE_SIZE + TILE_SIZE*4), (int) (VISIBLE_WORLD_DIMENSIONS.y*TILE_SIZE + TILE_SIZE*4));
		config.useVsync(true);
		config.setForegroundFPS(60);


		//FileHandling.createFile("aaaaa.txt");
		Maze maze = new Maze(32L, 32, 32);
		maze.genMaze();
		String[] mazeArray = new String[maze.maze.length];
		for (int i = 0; i < maze.maze.length; i++) {
			String mazeLine = "";
			for (int j = 0; j < maze.maze[i].length; j++) {
				mazeLine = mazeLine + maze.maze[i][j];
			}mazeArray[i] = mazeLine + "\n";
		}

		FileHandling.writeToFile("Maze.txt", mazeArray);


		config.setTitle("World Gen Tool");
		config.setWindowIcon("assets/tree.png");
		new Lwjgl3Application(new Init(), config);
	}


}
