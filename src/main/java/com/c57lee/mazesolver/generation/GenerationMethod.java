package com.c57lee.mazesolver.generation;

import com.c57lee.mazesolver.model.Cell;
import com.c57lee.mazesolver.model.Maze;
import com.c57lee.mazesolver.userinterface.controller.MazeController;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import java.util.Random;

public abstract class GenerationMethod {

    protected MazeController controller;
    protected Maze maze;
    protected Random myRandom;
    protected Thread thread;
    protected int sleepDuration = 50;
    public boolean genFinished;

    public GenerationMethod(int width, int height){
        this.maze = new Maze(width,height);
        this.myRandom = new Random();
        genFinished = false;
    }

    public void reset(){
        this.maze = new Maze(maze.getWidth(), maze.getHeight());
        genFinished = false;
    }

    public void startMazeGenTask(){
        Task<Void> task = getMazeGenTask();
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                genFinished = true;
                //controller.checkMaze();
            }
        });
        thread = new Thread(task);
        thread.setName("Generation Thread");
        thread.setDaemon(true);
        thread.start();
    }

    public abstract Task<Void> getMazeGenTask();

    public void setController(MazeController controller) {
        this.controller = controller;
    }

    public void connectNeighboringCells(Cell cell1, Cell cell2) {
        maze.connectNeighboringCells(cell1,cell2);
        updateUICell(cell1);
        updateUICell(cell2);
    }

    public void updateUICell(Cell cell) {
        controller.updateCell(cell);
    }

    public Maze getMaze(){
        return maze;
    }

    public void setSleepDuration(int sleepDuration) {
        this.sleepDuration = sleepDuration;
    }

    public boolean isGenFinished(){
        return genFinished;
    }
}
