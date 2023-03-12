package com.joelallison.generation;

import com.joelallison.graphics.Tileset;
import com.joelallison.screens.AppScreen;

import java.util.*;

import static com.joelallison.screens.AppScreen.tilesets;

public class MazeLayer extends Layer {
    //depth-first search (recursive) maze generation
    //(the stack typical of a DFS is via the call stack)
    //I've found that this has a limit of 857x857, due to a StackOverflowError.
    //While I'd love to write an algorithm without this limitation, I would say that an 857x857 maze is pretty good!

    private int width;
    private int height;
    public static final int MAX_ACROSS = 857;
    public static final int MIN_ACROSS = 3;
    private int count = 0;
    private boolean opaque = true;
    public int[][] maze; //I thought about using a 2D boolean array, but an int array makes the conversion to a file slightly simpler.
    public List<Tileset.MazeTileSpec> tileSpecs;
    public MazeLayer(int layerID, String name, long seed, int width, int height, String tilesetName, boolean opaque) {
        super(name, seed, layerID);

        //each 'wall' and 'path' tiles are full cells, so the maze needs to be an odd width and height
        //this means that, in a way, the maze is half the dimensions that were specified.
        this.width = width + ((width+1)%2); //ensures width is odd
        this.height = height + ((height+1)%2); //ensures height is odd
        this.tilesetName = tilesetName;
        this.opaque = opaque;

        this.tileSpecs = new ArrayList<>();
    }

    public MazeLayer(long seed) {
        super(seed);
        Random random = new Random();
        name = "Maze Layer";

        //each 'wall' and 'path' tiles are full cells, so the maze needs to be an odd width and height
        //this means that, in a way, the maze is half the dimensions that were specified.
        int dimension = random.nextInt(MIN_ACROSS, MAX_ACROSS / 4);
        //this.width = dimension + ((dimension+1)%2); //ensures width is odd
        //this.height = dimension + ((dimension+1)%2); //ensures height is odd
        this.width = 33;
        this.height = 33;
        this.tilesetName = "Walls";

        this.tileSpecs = new ArrayList<>();
    }

    @Override
    public void defaultTileValues() {
        Set<String> tiles = tilesets.get(this.tilesetName).map.keySet();

        //set tile values to be evenly distributed
        int tileNum = 0;
        for (String tile : tiles) {
            this.tileSpecs.add(new Tileset.MazeTileSpec(tile));
            tileNum++;
        }
    }

    @Override
    public void sortTileSpecs() {
        //using method outlined here: https://www.java67.com/2015/01/how-to-sort-hashmap-in-java-based-on.html
        final Comparator<Tileset.MazeTileSpec> NAME_COMPARATOR  = new Comparator<Tileset.MazeTileSpec>() {
            @Override
            public int compare(Tileset.MazeTileSpec t1, Tileset.MazeTileSpec t2){
                return t1.name.compareTo(t2.name);
            }
        };

        this.tileSpecs.sort(NAME_COMPARATOR);
    }

    public void genMaze() {
        count = 0;
        maze = new int[height][width];
        // initialize the 2D array with 1s
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                maze[i][j] = 1;
            }
        }

        //because of the seed, the random starting cell will always be the same if the seed and dimensions are the same.
        Random rand = new Random(seed);

        //r for row, c for column
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

        //recursively carve out the maze
        recursion(r, c);

        for(int y = 0; y < height; y++) {
            maze[y][0] = 1;
        }

        for(int x = 0; x < width; x++) {
            maze[0][x] = 1;
        }
    }

    private void recursion(int r, int c) {
        //put up, down, left, right in a random (seeded) order
        Integer[] randomDirections = generateRandomDirections();
        //count makes direction picking seeded - for a specific count and specific seed, shuffle will be the same
        //this means that for a particular seed, randomDirections gets shuffled the same way over the course of generation, making the carved out path be the same
        count++;
        //check each direction
        for (int i = 0; i < randomDirections.length; i++) {

            switch(randomDirections[i]) {
                case 1: //up
                    if (r - 2 <= 0) { //checks not at edge
                        continue;
                    }
                    if (maze[r - 2][c] != 0) { //if path cell above hasn't been carved out yet, go there
                        maze[r-2][c] = 0;
                        maze[r-1][c] = 0;
                        recursion(r - 2, c); //move to path cell above, do the process again
                    }
                    break;
                case 2: //right
                    if (c + 2 >= width - 1) { //checks not at edge
                        continue;
                    }
                    if (maze[r][c + 2] != 0) { //if path to the right hasn't been carved out yet, go there
                        maze[r][c + 2] = 0;
                        maze[r][c + 1] = 0;
                        recursion(r, c + 2); //move to path cell to the right, do the process again
                    }
                    break;
                case 3: //down
                    if (r + 2 >= height - 1) { //checks not at edge
                        continue;
                    }
                    if (maze[r + 2][c] != 0) { //if path cell below hasn't been carved out yet, go there
                        maze[r+2][c] = 0;
                        maze[r+1][c] = 0;
                        recursion(r + 2, c); //move to path cell to the right, do the process again
                    }
                    break;
                case 4: //left
                    if (c - 2 <= 0) { //checks not at edge
                        continue;
                    }
                    if (maze[r][c - 2] != 0) { //if path cell to the left hasn't been carved out yet, go there
                        maze[r][c - 2] = 0;
                        maze[r][c - 1] = 0;
                        recursion(r, c - 2); //move to path cell to the right, do the process again
                    }
                    break;
            }
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

    public boolean isOpaque() {
        return opaque;
    }

    public void setOpaque(boolean opaque) {
        this.opaque = opaque;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}