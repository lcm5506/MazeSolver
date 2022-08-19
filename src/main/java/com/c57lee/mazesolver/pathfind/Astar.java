package com.c57lee.mazesolver.pathfind;

import com.c57lee.mazesolver.model.Cell;
import com.c57lee.mazesolver.model.Maze;
import javafx.concurrent.Task;

import java.util.*;

public class Astar extends PathFind{
    HashMap<Cell,List<Cell>> map;
    public Astar(Maze maze) {
        super(maze);
        map = new HashMap<>();
    }

    @Override
    public Task<List<Cell>> getTask() {
        return new Task<List<Cell>>() {
            @Override
            protected List<Cell> call() throws Exception {
                List<Cell> current;
                Comparator<List<Cell>> comparator = new Comparator<List<Cell>>() {
                    @Override
                    public int compare(List<Cell> o1, List<Cell> o2) {
                        Cell c1 = o1.get(o1.size()-1);
                        Cell c2 = o2.get(o2.size()-1);
                        double ab1 = Math.abs(c1.getX()- finish.getX());
                        double bc1 = Math.abs(c1.getY()- finish.getY());
                        double ab2 = Math.abs(c2.getX()- finish.getX());
                        double bc2 = Math.abs(c2.getY()- finish.getY());
                        double weight1 = o1.size()+Math.hypot(ab1,bc1);
                        double weight2 = o2.size()+Math.hypot(ab2,bc2);
                        if (weight1>weight2)
                            return 1;
                        else if (weight1<weight2)
                            return -1;
                        else
                            return 0;
                    }
                };
                PriorityQueue<List<Cell>> priorityQueue = new PriorityQueue<>(comparator);
                PriorityQueue<List<Cell>> foundPaths = new PriorityQueue<>(new Comparator<List<Cell>>(){

                    @Override
                    public int compare(List<Cell> o1, List<Cell> o2) {
                        return Integer.compare(o1.size(), o2.size());
                    }
                });
                ArrayList<Cell> rootList = new ArrayList<>();
                rootList.add(start);
                priorityQueue.add(rootList);

                while(!priorityQueue.isEmpty()){
                    current = priorityQueue.poll();

                    Cell latest = current.get(current.size()-1);
                    controller.setCellSearched(latest);
                    if (latest.getX()== finish.getX() && latest.getY()== finish.getY()) {
                        foundPaths.add(current);
                        continue;
                    }
                    Cell previous;
                    if (current.size()>1)
                        previous = current.get(current.size()-2);
                    else
                        previous = null;

                    List<Cell> nextCells = maze.getOpenNeighbors(latest.getX(),latest.getY());
                    nextCells.remove(previous);
                    nextCells.removeAll(current);

                    // move to next vertex
                    while (nextCells.size()==1){
                        current.addAll(nextCells);
                        latest = current.get(current.size()-1);
                        controller.setCellSearched(latest);
                        if (latest.getX()== finish.getX() && latest.getY()== finish.getY()) {
                            foundPaths.add(current);
                            break;
                        }
                        if (current.size()>1)
                            previous = current.get(current.size()-2);
                        else
                            previous = null;

                        nextCells = maze.getOpenNeighbors(latest.getX(),latest.getY());
                        nextCells.remove(previous);
                        nextCells.removeAll(current);
                        Thread.sleep(sleepDuration);
                    }

                    // Case: reached cross road that is at least 3 way. These are 'vertices' in graph representation of maze.
                    if (nextCells.size()>1){
                        Cell last =current.get(current.size()-1);
                        if (map.containsKey(last)){
                            if (map.get(last).size() > current.size()){
                                map.replace(last,current);
                                priorityQueue.removeIf(l->l.contains(last));
                                for (Cell c: nextCells) {
                                    List<Cell> nextPath = new ArrayList<>(current);
                                    nextPath.add(c);
                                    priorityQueue.add(nextPath);
                                }
                            }
                        } else {
                            map.put(last,current);
                            for (Cell c: nextCells) {
                                List<Cell> nextPath = new ArrayList<>(current);
                                nextPath.add(c);
                                priorityQueue.add(nextPath);
                            }
                        }
                        Thread.sleep(sleepDuration);
                    }

                }

                for (Cell c: map.keySet()){
                    System.out.println(c+" : "+map.get(c));
                }

                for (List<Cell> path: foundPaths){
                    controller.setAllPathFound(path);
                    System.out.println(path.size());
                }

                System.out.println("Number of Paths Found: "+foundPaths.size());

                return foundPaths.poll();
            }
        };
    }

    public double getDistance(Cell c1, Cell c2){
        double ab = Math.abs(c1.getX()-c2.getX());
        double bc = Math.abs(c1.getY()-c2.getY());
        return Math.hypot(ab,bc);
    }

    @Override
    public void getPathWithoutTask() {

    }
}
