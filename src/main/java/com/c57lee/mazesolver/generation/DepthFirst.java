package com.c57lee.mazesolver.generation;

import com.c57lee.mazesolver.model.Cell;
import javafx.concurrent.Task;

import java.util.List;
import java.util.Stack;

public class DepthFirst extends GenerationMethod {
    Cell start;

    public DepthFirst(int width, int height) {
        super(width, height);
    }

    public Task getMazeGenTask(){

        return new Task<Void>(){

            @Override
            protected Void call() throws Exception {
                setRandomStart();
                Stack<Cell> cStack = new Stack<>();
                cStack.push(start);
                while (!cStack.isEmpty()){
                    Cell currCell = cStack.pop();
                    currCell.setVisited(true);
                    updateUICell(currCell);
                    List<Cell> unvisitedNeighbors = maze.getUnvisitedNeighbors(currCell.getX(), currCell.getY());
                    if (!unvisitedNeighbors.isEmpty()) {
                        cStack.push(currCell);
                        int index = myRandom.nextInt(unvisitedNeighbors.size());
                        Cell randomUnvisitedNeighbor = unvisitedNeighbors.get(index);
                        connectNeighboringCells(currCell,randomUnvisitedNeighbor);
                        randomUnvisitedNeighbor.setVisited(true);
                        cStack.push(randomUnvisitedNeighbor);
                    }
                     Thread.sleep(sleepDuration);
                }
                System.out.println("Task Complete");
                return null;
            }
        };
    }

    public Cell setRandomStart() {
        int startX = myRandom.nextInt(maze.getWidth());
        int startY = myRandom.nextInt(maze.getHeight());
        this.start = maze.getCell(startX,startY);
        return start;
    }


}
