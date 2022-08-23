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
        return new Task<>() {
            @Override
            protected List<Cell> call() throws Exception {
                Queue<List<Cell>> queue = new ConcurrentLinkedQueue<>();
                ArrayList<Cell> rootList = new ArrayList<>();
                rootList.add(start);
                queue.add(rootList);

                while (!queue.isEmpty()) {
                    List<Cell> current = queue.poll();
                    Cell latest = current.get(current.size() - 1);
                    latest.setVisited(true);
                    controller.setCellSearched(latest);

                    Cell previous = null;
                    if (current.size() > 1)
                        previous = current.get(current.size() - 2);

                    List<Cell> nextList = maze.getUnvisitedOpenNeighbors(latest);
                    nextList.remove(previous);

                    if (nextList.isEmpty())
                        continue;
                    for (Cell next : nextList) {
                        ArrayList<Cell> nextPath = new ArrayList<>(current);
                        List<Cell> edge = getEdge(latest, next);
                        if (edge.contains(finish)) {
                            nextPath.addAll(edge.subList(0, edge.indexOf(finish) + 1));
                            return nextPath;
                        }
                        nextPath.addAll(edge);
                        queue.add(nextPath);
                    }
                    Thread.sleep(sleepDuration);

                }
                return null;
            }
        };
    }

}
