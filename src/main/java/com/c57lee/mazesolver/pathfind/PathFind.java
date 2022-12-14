package com.c57lee.mazesolver.pathfind;

import com.c57lee.mazesolver.model.Cell;
import com.c57lee.mazesolver.model.Maze;
import com.c57lee.mazesolver.userinterface.controller.MazeController;
import com.c57lee.mazesolver.util.TaskFinishedHandler;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class PathFind {

    protected Maze maze;
    protected Cell start,finish;
    protected MazeController controller;
    protected int sleepDuration=controller.INITIAL_SLEEP_DURATION;
    protected List<Cell> pathFound;
    protected TaskFinishedHandler handler;

    public PathFind(Maze maze){
        this.maze = maze;
        this.maze.setMazeUnvisited();
        pathFound = new ArrayList<>();
    }

    public void reset(){
        pathFound.clear();
        maze.setMazeUnvisited();
    }

    public void setStart(int x, int y){
        this.start = maze.getCell(x,y);
    }

    public void setFinish(int x, int y){
        this.finish = maze.getCell(x,y);
    }

    public void setController(MazeController controller){
        this.controller = controller;
    }

    public void setSleepDuration(int duration){
        this.sleepDuration = duration;
    }
    public void setMazeUnvisited(){
        for (int i=0; i<maze.getWidth(); i++){
            for (int j=0; j<maze.getHeight(); j++){
                if (maze.getCell(i,j).isVisited()) {
                    System.out.println("Cell at "+i+","+j+" is visited");
                    maze.getCell(i, j).setVisited(false);
                }
                if (maze.getCell(i,j).isVisited()) {
                    System.out.println("Cell at "+i+","+j+" is visited");
                    maze.getCell(i, j).setVisited(false);
                }
            }
        }
    }

    public void findPath(){
        Task<List<Cell>> task = getTask();
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Task Succeeded.");
                pathFound.clear();
                if (task.getValue()!= null)
                    pathFound.addAll(task.getValue());
                controller.setPathFound(pathFound);
                if (Objects.nonNull(handler))
                    handler.onTaskFinished();
            }
        });

        Thread thread = new Thread(task);
        thread.setName("Path Find Thread");
        thread.setDaemon(true);
        thread.start();
    }

    public abstract Task<List<Cell>> getTask();

    public int getMazeWidth(){
        return maze.getWidth();
    }

    public int getMazeHeight(){
        return maze.getHeight();
    }

    public Cell getStart(){
        return start;
    }

    public Cell getFinish(){
        return finish;
    }

    public List<Cell> getEdge(Cell start, Cell next){
        List<Cell> edge = new ArrayList<>();
        edge.add(start);
        edge.add(next);
        Cell latest = next;
        latest.setVisited(true);
        controller.setCellSearched(latest);
        Cell previous = start;

        List<Cell> nextCells = maze.getOpenNeighbors(latest);

        while (nextCells.size()==1){
            edge.addAll(nextCells);
            latest = edge.get(edge.size()-1);
            latest.setVisited(true);
            controller.setCellSearched(latest);

            nextCells = maze.getOpenNeighbors(latest);
            nextCells.remove(previous);
            nextCells.remove(start); // To prevent possible loop.
            try {
                Thread.sleep(sleepDuration);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            previous = latest;
        }
        return edge;
    }

    public void setTaskFinishedHandler(TaskFinishedHandler handler){
        this.handler = handler;
    }


}
