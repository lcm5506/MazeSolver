package com.c57lee.mazesolver.pathfind;

import com.c57lee.mazesolver.model.*;
import com.c57lee.mazesolver.userinterface.controller.MazeController;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class DepthFirstSearch extends PathFind{

    public DepthFirstSearch(Maze maze){
        super(maze);
    }

    public Task<List<Cell>> getTask(){
        return new Task<List<Cell>>(){

            @Override
            protected List<Cell> call() throws Exception {
                List<Cell> currentPath = new ArrayList<>();
                Stack<List<Cell>> stack = new Stack<>();
                ArrayList<Cell> rootList = new ArrayList<>();
                rootList.add(start);
                stack.add(rootList);

                while (!stack.isEmpty()){
                    currentPath = stack.pop();

                    Cell latest = currentPath.get(currentPath.size()-1);
                    latest.setVisited(true);
                    controller.setCellSearched(latest);
                    if (latest.getX() == finish.getX() && latest.getY() == finish.getY()){
                        break;
                    }

                    Cell previous;
                    if (currentPath.size()<2)
                        previous = null;
                    else
                        previous = currentPath.get(currentPath.size()-2);

                    List<Cell> nextList = maze.getUnvisitedOpenNeighbors(latest.getX(), latest.getY());
                    nextList.remove(previous);
                    if (!nextList.isEmpty()) {
                        for (Cell next: nextList) {
                            ArrayList<Cell> nextPath = new ArrayList<>(currentPath);
                            nextPath.add(next);
                            stack.push(nextPath);
                        }
                    }
                    Thread.sleep(sleepDuration);

                }
                System.out.println("last Cell in currentpath : "+currentPath.get(currentPath.size()-1));
                return currentPath;
            }
        };
    }

}
