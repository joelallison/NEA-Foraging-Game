package com.joelallison.generation;

import java.util.*;

public class MazeLayer extends Layer {
    //depth-first search (recursive) maze generation
    //(the stack typical of a DFS is via the call stack)
    //I've found that this has a limit of 857x857, due to a StackOverflowError.
    //While I'd love to write an algorithm without this limitation, I would say that an 857x857 maze is pretty good!

    private long seed;
    private int width;
    private int height;

    private int count = 0;

    public int[][] maze;
    public MazeLayer(String name, long seed, int width, int height) {
        super(name, seed);

        //each 'wall' and 'path' tiles are full cells, so the maze needs to be an odd width and height
        //this means that, in a way, the maze is half the dimensions that were specified.
        this.width = width + ((width+1)%2); //ensures width is odd
        this.height = height + ((height+1)%2); //ensures height is odd
    }

    public void genMaze() {
        maze = new int[height][width];
        // initialize the 2D array with 1s
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                maze[i][j] = 1;
            }
        }

        //because of the seed, the random starting cell will always be the same if the seed and dimensions are the same.
        Random rand = new Random(seed);

        //r for row c for column
        //random odd number row
        int r = rand.nextInt(height);
        while (r % 2 == 0) {
            r = rand.nextInt(height);
        }

        //random odd number column
        int c = rand.nextInt(width);
        while (c % 2 == 0) {
            c = rand.nextInt(width);
        }

        //starting cell
        maze[r][c] = 0;

        System.out.println(r + " " + c);
        //recursively carve out the maze
        recursion(r, c);
    }
    private void recursion(int r, int c) {
        //put up, down, left, right in a random (seeded) order
        Integer[] randomDirections = generateRandomDirections();
        count++;
        //check each direction
        for (int i = 0; i < randomDirections.length; i++) {

            switch(randomDirections[i]) {
                case 1: //up
                    if (r - 2 <= 0) {
                        continue;
                    }
                    if (maze[r - 2][c] != 0) {
                        maze[r-2][c] = 0;
                        maze[r-1][c] = 0;
                        recursion(r - 2, c);
                    }
                    break;
                case 2: //right
                    if (c + 2 >= width - 1) {
                        continue;
                    }
                    if (maze[r][c + 2] != 0) {
                        maze[r][c + 2] = 0;
                        maze[r][c + 1] = 0;
                        recursion(r, c + 2);
                    }
                    break;
                case 3: //down
                    if (r + 2 >= height - 1) {
                        continue;
                    }
                    if (maze[r + 2][c] != 0) {
                        maze[r+2][c] = 0;
                        maze[r+1][c] = 0;
                        recursion(r + 2, c);
                    }
                    break;
                case 4: //left
                    if (c - 2 <= 0) {
                        continue;
                    }
                    if (maze[r][c - 2] != 0) {
                        maze[r][c - 2] = 0;
                        maze[r][c - 1] = 0;
                        recursion(r, c - 2);
                    }
                    break;
            }//recursively backtracks just due to trying directions again and again
        }
    }

    private Integer[] generateRandomDirections() {
        ArrayList<Integer> randoms = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            randoms.add(i + 1);

        //this allows the maze generation to be seeded
        Collections.shuffle(randoms, new Random(seed+count*1024L));

        return randoms.toArray(new Integer[4]);
    }
}
