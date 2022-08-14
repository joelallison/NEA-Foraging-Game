package com.joelallison.level;

import tools.OpenSimplex2S;

import java.util.*;

public class Maze {

    //depth-first search recursive maze generation
    private long seed;
    private int width;
    private int height;

    private int count = 0;

    public int[][] maze;
    public Maze(long seed, int width, int height){
        this.seed = seed;
        this.width = width + ((width+1)%2);
        this.height = height + ((height+1)%2);
    }

    public int[][] genMaze(){
        maze = new int[height][width];
        // Initialize
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                maze[i][j] = 1;
            }
        }


        Random rand = new Random(seed);
        // r for row、c for column
        // Generate random r
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
    public void recursion(int r, int c) {
        // 4 random directions
        int[] randDirs = generateRandomDirections();
        count++;
        // Examine each direction
        for (int i = 0; i < randDirs.length; i++) {
            switch(randDirs[i]){
                case 1: // Up
                    //　Whether 2 cells up is out or not
                    if (r - 2 <= 0)
                        continue;
                    if (maze[r - 2][c] != 0) {
                        maze[r-2][c] = 0;
                        maze[r-1][c] = 0;
                        recursion(r - 2, c);
                    }
                    break;
                case 2: // Right
                    // Whether 2 cells to the right is out or not
                    if (c + 2 >= width - 1)
                        continue;
                    if (maze[r][c + 2] != 0) {
                        maze[r][c + 2] = 0;
                        maze[r][c + 1] = 0;
                        recursion(r, c + 2);
                    }
                    break;
                case 3: // Down
                    // Whether 2 cells down is out or not
                    if (r + 2 >= height - 1)
                        continue;
                    if (maze[r + 2][c] != 0) {
                        maze[r+2][c] = 0;
                        maze[r+1][c] = 0;
                        recursion(r + 2, c);
                    }
                    break;
                case 4: // Left
                    // Whether 2 cells to the left is out or not
                    if (c - 2 <= 0)
                        continue;
                    if (maze[r][c - 2] != 0) {
                        maze[r][c - 2] = 0;
                        maze[r][c - 1] = 0;
                        recursion(r, c - 2);
                    }
                    break;
            }
        }

    }

    /**
     * Generate an array with random directions 1-4
     * @return Array containing 4 directions in random order
     */
    public int[] generateRandomDirections() {
        return seededFisherYatesShuffle(new int[]{0, 1, 2, 3});
    }

    /* Based on wikipedia description of the 'modern version':

    -- To shuffle an array a of n elements (indices 0..n-1):
        for i from n−1 downto 1 do
            j ← random integer such that 0 ≤ j ≤ i
            exchange a[j] and a[i]
     */
    public int[] seededFisherYatesShuffle(int[] arr) {
        int[] a = arr;
        for (int i = arr.length-1; i > 1; i--) {
            count++;
            int j = (int) (i*Math.abs(OpenSimplex2S.noise2(seed, 128*count, 0)));
            int temp = a[j];
            a[j] = a[i];
            a[i] = temp;
        }

        return arr;
    }
}
