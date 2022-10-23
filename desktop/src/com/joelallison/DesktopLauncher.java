package com.joelallison;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.joelallison.generation.FileHandling;
import com.joelallison.generation.Maze;
import com.joelallison.screens.Init;

import static com.joelallison.screens.GameScreen.ASPECT_RATIO;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode((int) ASPECT_RATIO.x * 320, (int) ASPECT_RATIO.y * 320);
		config.useVsync(true);
		config.setForegroundFPS(60);


		//FileHandling.createFile("aaaaa.txt");
		int size = 33;
		Maze maze = new Maze(1, size,  size);
		maze.genMaze();

		FileHandling.writeToFile("Maze.txt", FileHandling.mazeToStringArray(maze.maze, "pgm", true, true));

		config.setTitle("World Gen Tool");
		config.setWindowIcon("assets/tree.png");
		new Lwjgl3Application(new Init(), config);
	}


}
