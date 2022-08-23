package com.c57lee.mazesolver.generation;

import com.c57lee.mazesolver.model.Cell;
import com.c57lee.mazesolver.model.Maze;
import com.c57lee.mazesolver.model.Wall;
import javafx.concurrent.Task;

import java.util.*;

public class Kruskal extends GenerationMethod{

    Thread thread;

    public Kruskal(int width, int height){
        super(width,height);
    }

    public Stack<Wall> createWallStack(){
        Stack<Wall> walls = new Stack<>();
        // horizontal walls
        for (int i=0; i<maze.getWidth(); i++){
            for (int j=0; j<maze.getHeight()-1; j++){
                walls.push(new Wall(maze.getCell(i,j),maze.getCell(i,j+1)));
            }
        }
        // vertical walls
        for (int i=0; i<maze.getWidth()-1; i++){
            for (int j=0; j< maze.getHeight(); j++){
                walls.push(new Wall(maze.getCell(i,j),maze.getCell(i+1,j)));
            }
        }
        return walls;
    }



    public Task<Void> getMazeGenTask(){
        return new Task<Void>(){
            @Override
            protected Void call() throws Exception {
                Stack<Wall> walls = createWallStack();
                List<Wall> closedWalls = new ArrayList<>();
                CellSets cellSets = new CellSets(maze);

                Collections.shuffle(walls);

                while (!walls.isEmpty()){
                    Wall wall = walls.pop();
                    Cell cell1 = wall.getCell1();
                    Cell cell2 = wall.getCell2();
                    cell1.setVisited(true);
                    cell2.setVisited(true);
                    updateUICell(cell1);
                    updateUICell(cell2);
                    if (cellSets.isInDistinctSets(cell1,cell2)){
                        connectNeighboringCells(cell1, cell2);
                        cellSets.mergeSetsContaining(cell1, cell2);
                    } else {
                        closedWalls.add(wall);
                    }
                    //Thread.sleep(getSleepDuration());
                }

                // Remove few random walls to possibly create loops and non-perfect maze
//                for (int i=0; i<600; i++){
//                    Wall removedWall = closedWalls.remove(myRandom.nextInt(closedWalls.size()));
//                    Cell cell1 = removedWall.getCell1();
//                    Cell cell2 = removedWall.getCell2();
//                    connectNeighboringCells(cell1, cell2);
//                    cellSets.mergeSetsContaining(cell1, cell2);
//                    //Thread.sleep(getSleepDuration());
//                }

                return null;
            }
        };
    }


    public static class CellSets {
        private final List<Set<Cell>> setsList;

        public CellSets(Maze maze){
            setsList = new ArrayList<>();
            createSets(maze);
        }

        public void createSets(Maze maze){
            for (int i=0; i<maze.getWidth(); i++){
                for (int j=0; j<maze.getHeight(); j++){
                    Set<Cell> set = new HashSet<>();
                    set.add(maze.getCell(i,j));
                    setsList.add(set);
                }
            }
        }

        public Set<Cell> getSetContaining(Cell cell){
            for (Set<Cell> set: setsList){
                if (set.contains(cell)) {
                    return set;
                }
            }
            System.out.println("setContaining cell NOT FOUND");
            return null;
        }

        public boolean isInDistinctSets(Cell cell1, Cell cell2){
            return !getSetContaining(cell1).equals(getSetContaining(cell2));
        }

        public void mergeSetsContaining(Cell cell1, Cell cell2){
            Set<Cell> set1 = getSetContaining(cell1);
            Set<Cell> set2 = getSetContaining(cell2);
            if (!set1.equals(set2)) {
                setsList.remove(set2);
                set1.addAll(set2);
            }
        }

        public int getSize(){
            return setsList.size();
        }
    }

}
