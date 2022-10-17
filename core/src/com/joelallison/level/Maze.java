package com.joelallison.level;

import tools.OpenSimplex2S;

import java.util.*;

public class Maze {
    //depth-first search recursive maze generation
    //I've found that this has a limit of 857x857

    private long seed;
    private int width;
    private int height;

    private int count = 0;

    public int[][] maze;
    public Maze(long seed, int width, int height){
        this.seed = seed;
        this.width = width + ((width+1)%2); //ensures width is odd
        this.height = height + ((height+1)%2); //ensures height is odd
    }

    public int[][] genMaze() {
        maze = new int[height][width];
        // Initialize
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                maze[i][j] = 1;
            }
        }

        Random rand = new Random(seed);
        // r for row c for column
        // Generate random odd number r
        int r = rand.nextInt(height);
        while (r % 2 == 0) {
            r = rand.nextInt(height);
        }

        // Generate random c
        int c = rand.nextInt(width);
        while (c % 2 == 0) {
            c = rand.nextInt(width);
        }
        // Starting cell
        maze[r][c] = 0;

        System.out.println(r + " " + c);
        //　Allocate the maze with recursive method
        recursion(r, c);

        return maze;
    }
    private void recursion(int r, int c) {
        // 4 random directions
        Integer[] randDirs = generateRandomDirections();
        count++;
        // Examine each direction
        for (int i = 0; i < randDirs.length; i++) {

            switch(randDirs[i]) {
                case 1: // Up
                    //　Whether 2 cells up is out or not
                    if (r - 2 <= 0) {
                        continue;
                    }
                    if (maze[r - 2][c] != 0) {
                        maze[r-2][c] = 0;
                        maze[r-1][c] = 0;
                        recursion(r - 2, c);
                    }
                    break;
                case 2: // Right
                    // Whether 2 cells to the right is out or not
                    if (c + 2 >= width - 1) {
                        continue;
                    }
                    if (maze[r][c + 2] != 0) {
                        maze[r][c + 2] = 0;
                        maze[r][c + 1] = 0;
                        recursion(r, c + 2);
                    }
                    break;
                case 3: // Down
                    // Whether 2 cells down is out or not
                    if (r + 2 >= height - 1) {
                        continue;
                    }
                    if (maze[r + 2][c] != 0) {
                        maze[r+2][c] = 0;
                        maze[r+1][c] = 0;
                        recursion(r + 2, c);
                    }
                    break;
                case 4: // Left
                    // Whether 2 cells to the left is out or not
                    if (c - 2 <= 0) {
                        continue;
                    }
                    if (maze[r][c - 2] != 0) {
                        maze[r][c - 2] = 0;
                        maze[r][c - 1] = 0;
                        recursion(r, c - 2);
                    }
                    break;
            }
        }
    }

    private Integer[] generateRandomDirections() {
        ArrayList<Integer> randoms = new ArrayList<Integer>();
        for (int i = 0; i < 4; i++)
            randoms.add(i + 1);
        Collections.shuffle(randoms, new Random(seed+count*1024));

        return randoms.toArray(new Integer[4]);
    }
}
