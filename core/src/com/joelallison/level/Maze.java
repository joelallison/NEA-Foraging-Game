package com.joelallison.level;

import tools.OpenSimplex2S;

import java.util.*;

public class Maze {
    private Queue<int[]> queue;
    private long seed;
    private int width;
    private int height;

    private Cell[][] grid;

    public Maze(long seed, int width, int height){
        this.seed = seed;
        this.width = width;
        this.height = height;
    }

    //noise2 as random generation, with i as x, and y as 0

    //based on description of a depth-first search algorithm from:
    //https://www.algosome.com/articles/maze-generation-depth-first.html

    //'N' is currentNode, 'A' is adjacentNode

    public void generateMaze() {


        //step 1
        int[] startPosition = randomlyChooseCell();
        int[] currentNode = new int[] {startPosition[0], startPosition[1]};

        //step 2
        queue.add(currentNode);
        int count = 0;
        while(!queue.isEmpty()) {

            //step 3
            grid[currentNode[0]][currentNode[1]].setVisited(true);

            //step 4
            List<String> availableDirections = Arrays.asList("UP", "RIGHT", "DOWN", "LEFT");
            if(grid[currentNode[0]+1][currentNode[1]].isVisited()){
                availableDirections.remove("UP");
            }if(grid[currentNode[0]][currentNode[1]+1].isVisited()){
                availableDirections.remove("RIGHT");
            }if(grid[currentNode[0]-1][currentNode[1]].isVisited()){
                availableDirections.remove("DOWN");
            }if(grid[currentNode[0]][currentNode[1]-1].isVisited()){
                availableDirections.remove("LEFT");
            }

            if(availableDirections.size() > 0) {
                float generatedDirection = (availableDirections.size()*Math.abs(OpenSimplex2S.noise2(seed, count, 0))) - 1;
                for (int i = availableDirections.size(); i > 0; i--) {
                    if(generatedDirection >= i) {
                        String chosenDirection = availableDirections.get(i);
                    }
                }
            }else {

            }


        }


    }

    public void initializeGrid() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new Cell();
            }
        }
    }


    private int[] randomlyChooseCell() {
        return new int[] {(int) (width*Math.abs(OpenSimplex2S.noise2(seed, -1, 0))), (int)(height*Math.abs(OpenSimplex2S.noise2(seed, 0, -1)))};
    }
}
