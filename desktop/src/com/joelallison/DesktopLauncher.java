package com.joelallison;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.joelallison.generation.FileHandling;
import com.joelallison.screens.TitleScreen;

import static com.joelallison.screens.MainScreen.LEVEL_ASPECT_RATIO;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode((int) LEVEL_ASPECT_RATIO.x * 320, (int) LEVEL_ASPECT_RATIO.y * 320);
		config.useVsync(true);
		config.setForegroundFPS(60);

		//FileHandling.createFile("aaaaa.txt");
		//int size = 33;
		//Maze maze = new Maze("a maze ing",1, size,  size);
		//maze.genMaze();
		//FileHandling.writeToFile("Maze.txt", FileHandling.mazeToStringArray(maze.maze, "pgm", true, true));

		config.setTitle("World Gen Tool");
		//config.setWindowIcon("assets/tree.png");
		new Lwjgl3Application(new TitleScreen(), config);
	}
}
