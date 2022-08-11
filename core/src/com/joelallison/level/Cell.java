package com.joelallison.level;

public class Cell {
    private boolean visited;

    //false = no wall, true = wall,
    //layout --> [top, right, bottom, left]
    private boolean[] walls = new boolean[] {true, true, true, true};


    public Cell(){
        this.visited = false;
    }
    public Cell(boolean visited) {
        this.visited = visited;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean[] getWalls() {
        return walls;
    }

    public void setWalls(boolean[] walls) {
        this.walls = walls;
    }

    public void setWall(int index, boolean value){
        this.walls[index] = value;
    }
}
