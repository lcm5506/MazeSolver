package com.c57lee.mazesolver.pathfind;

import com.c57lee.mazesolver.model.Cell;
import com.c57lee.mazesolver.model.Maze;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BreadthFirstSearch extends PathFind {


    public BreadthFirstSearch(Maze maze) {
        super(maze);
    }

    @Override
    public Task<List<Cell>> getTask() {
        return new Task<List<Cell>>() {
            @Override
            protected List<Cell> call() throws Exception {
                List<Cell> currentPath = new ArrayList<>();
                Queue<List<Cell>> queue = new ConcurrentLinkedQueue<>();
                ArrayList<Cell> rootList = new ArrayList<>();
                rootList.add(start);
                queue.add(rootList);

                while (!queue.isEmpty()){
                    currentPath = queue.poll();
                    Cell latest = currentPath.get(currentPath.size()-1);
                    latest.setVisited(true);
                    controller.setCellSearched(latest);

                    if (latest.getX() == finish.getX() && latest.getY() == finish.getY()){
                        pathFound = currentPath;
                        break;
                    }

                    Cell previous;
                    if (currentPath.size()<2)
                        previous = null;
                    else
                        previous = currentPath.get(currentPath.size()-2);

                    List<Cell> nextList = maze.getUnvisitedOpenNeighbors(latest.getX(), latest.getY());
                    // nextList.remove(previous);
                    //controller.setPreviousNextCells(nextList);
                    if (!nextList.isEmpty()) {
                        for (Cell next: nextList) {
                            ArrayList<Cell> nextPath = new ArrayList<>(currentPath);
                            nextPath.add(next);
                            queue.add(nextPath);
                        }
                        Thread.sleep(sleepDuration);
                    }
                }
                return currentPath;
            }
        };
    }

}
