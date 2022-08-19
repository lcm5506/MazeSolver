package com.c57lee.mazesolver.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Maze {
    Cell[][] maze;
    int width, height;

    public Maze(int width, int height) {
        this.width = width;
        this.height = height;

        this.maze = new Cell[width][height];
        for (int i=0; i<width; i++){
            for (int j=0; j<height; j++){
                maze[i][j] = new Cell(i,j);
            }
        }
    }

    public List<Cell> getNeighbors(int x, int y){
        List<Cell>  neighbors = new ArrayList<>();
        if (x > 0)
            neighbors.add(maze[x-1][y]);
        if (y > 0)
            neighbors.add(maze[x][y-1]);
        if (x < width-1)
            neighbors.add(maze[x+1][y]);
        if (y < height-1)
            neighbors.add(maze[x][y+1]);
        return neighbors;
    }

    public List<Cell> getUnvisitedNeighbors(int x, int y){
        return getNeighbors(x,y).stream().filter(c->!c.isVisited()).toList();
    }

    public List<Cell> getUnvisitedNeighbors(Cell c){
        int x = c.getX();
        int y = c.getY();
        return getUnvisitedNeighbors(x,y);

    }

    public List<Cell> getOpenNeighbors(int x, int y){
        List<Cell> openNeighbors = new ArrayList<>();
        Cell cell = maze[x][y];
        if (y > 0){
            if (cell.isTopOpen())
                openNeighbors.add(maze[x][y-1]);
        }
        if (y < height-1) {
            if (cell.isBottomOpen())
                openNeighbors.add(maze[x][y+1]);
        }
        if (x > 0) {
            if (cell.isLeftOpen())
                openNeighbors.add(maze[x-1][y]);
        }
        if (x < width-1){
            if (cell.isRightOpen())
                openNeighbors.add(maze[x+1][y]);
        }
        return openNeighbors;
    }

    public List<Cell> getOpenNeighbors(Cell c){
        int x = c.getX();
        int y = c.getY();
        return getOpenNeighbors(x,y);
    }


    public List<Cell> getUnvisitedOpenNeighbors(int x, int y) {
        List<Cell> unvisitedOpenNeighbors = new ArrayList<>();
        if (y > 0){
            if (maze[x][y].isTopOpen()&&!maze[x][y-1].isVisited())
                unvisitedOpenNeighbors.add(maze[x][y-1]);
        }
        if (y < height-1) {
            if (maze[x][y].isBottomOpen()&&!maze[x][y+1].isVisited())
                unvisitedOpenNeighbors.add(maze[x][y+1]);
        }
        if (x > 0) {
            if (maze[x][y].isLeftOpen()&&!maze[x-1][y].isVisited())
                unvisitedOpenNeighbors.add(maze[x-1][y]);
        }
        if (x < width-1){
            if (maze[x][y].isRightOpen()&&!maze[x+1][y].isVisited())
                unvisitedOpenNeighbors.add(maze[x+1][y]);
        }
        return unvisitedOpenNeighbors;
    }

    public List<Cell> getUnvisitedOpenNeighbors(Cell c){
        int x = c.getX();
        int y = c.getY();
        return getUnvisitedOpenNeighbors(x,y);
    }

    public Cell getCell(int x, int y){
        return maze[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void connectNeighboringCells(Cell cell1, Cell cell2){
        cell1 = getCell(cell1.getX(),cell1.getY());
        cell2 = getCell(cell2.getX(),cell2.getY());
        int deltaX = cell1.getX()-cell2.getX();
        int deltaY = cell1.getY()-cell2.getY();
        if (deltaX == 1) {
            cell1.setLeftOpen(true);
            cell2.setRightOpen(true);
        } else if (deltaX == -1) {
            cell1.setRightOpen(true);
            cell2.setLeftOpen(true);
        } else if (deltaY == 1) {
            cell1.setTopOpen(true);
            cell2.setBottomOpen(true);
        } else if (deltaY == -1) {
            cell1.setBottomOpen(true);
            cell2.setTopOpen(true);
        }
    }

    public void setMazeUnvisited(){
        for (int i=0; i<width; i++){
            for (int j=0; j<height; j++){
                if (maze[i][j].isVisited()) {
                    maze[i][j].setVisited(false);
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Maze maze1 = (Maze) o;
        return width == maze1.width && height == maze1.height && Arrays.equals(maze, maze1.maze);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(width, height);
        result = 31 * result + Arrays.hashCode(maze);
        return result;
    }
}
