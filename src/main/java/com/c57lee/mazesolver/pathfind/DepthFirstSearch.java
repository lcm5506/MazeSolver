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
                Stack<List<Cell>> stack = new Stack<>();
                ArrayList<Cell> rootList = new ArrayList<>();
                rootList.add(start);
                stack.add(rootList);

                while (!stack.isEmpty()){
                    List<Cell> current = stack.pop();

                    Cell latest = current.get(current.size()-1);
                    latest.setVisited(true);
                    controller.setCellSearched(latest);

                    Cell previous = null;
                    if (current.size()>1)
                        previous = current.get(current.size()-2);

                    List<Cell> nextList = maze.getUnvisitedOpenNeighbors(latest);
                    nextList.remove(previous);

                    if (nextList.isEmpty())
                        continue;

                    for (Cell next: nextList) {
                        ArrayList<Cell> nextPath = new ArrayList<>(current);
                        List<Cell> edge = getEdge(latest,next);
                        if (edge.contains(finish)){
                            nextPath.addAll(edge.subList(0,edge.indexOf(finish)+1));
                            return nextPath;
                        }
                        nextPath.addAll(edge);
                        stack.push(nextPath);
                    }

                    Thread.sleep(sleepDuration);

                }

                return null;
            }
        };
    }

}
