package com.c57lee.mazesolver.pathfind;

import com.c57lee.mazesolver.model.Cell;
import com.c57lee.mazesolver.model.Maze;
import com.c57lee.mazesolver.model.Vertex;
import javafx.concurrent.Task;


import java.util.*;

public class Dijkstra extends PathFind{

    HashMap<Cell,Integer> map;
    HashMap<Cell,List<List<Cell>>> edgeMap;
    public Dijkstra(Maze maze) {
        super(maze);
        map = new HashMap<>();
    }

    @Override
    public Task<List<Cell>> getTask() {
        return new Task<List<Cell>>() {
            @Override
            protected List<Cell> call() throws Exception {
                List<Cell> current = new ArrayList<>();
                Comparator<List<Cell>> comparator = new Comparator<List<Cell>>() {
                    @Override
                    public int compare(List<Cell> o1, List<Cell> o2) {
                        if (o1.size()>o2.size())
                            return 1;
                        else if (o1.size()== o2.size())
                            return 0;
                        else
                            return -1;
                    }
                };
                PriorityQueue<List<Cell>> priorityQueue = new PriorityQueue<>(comparator);
                PriorityQueue<List<Cell>> foundPaths = new PriorityQueue<>(comparator);
                ArrayList<Cell> rootList = new ArrayList<>();
                rootList.add(start);
                priorityQueue.add(rootList);

                while(!priorityQueue.isEmpty()){
                    current = priorityQueue.poll();
                    Cell latest = current.get(current.size()-1);
                    latest.setVisited(true);
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
                        latest.setVisited(true);
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

                    if (nextCells.size()>1){
                        for (Cell c: nextCells) {
                            List<Cell> nextPath = new ArrayList<>(current);
                            nextPath.add(c);

                            if (map.containsKey(c)){
                                if (map.get(c)>nextPath.size()){
                                    priorityQueue.removeIf(l->l.contains(c));
                                    priorityQueue.add(nextPath);
                                    map.replace(c,nextPath.size());
                                }
                            } else {
                                priorityQueue.add(nextPath);
                                map.put(c,nextPath.size());
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

    public void getPathWithoutTask(){
        List<Cell> current = new ArrayList<>();
        Comparator<List<Cell>> comparator = new Comparator<List<Cell>>() {
            @Override
            public int compare(List<Cell> o1, List<Cell> o2) {
                if (o1.size()>o2.size())
                    return 1;
                else if (o1.size()== o2.size())
                    return 0;
                else
                    return -1;
            }
        };
        PriorityQueue<List<Cell>> priorityQueue = new PriorityQueue<>(comparator);
        PriorityQueue<List<Cell>> foundPaths = new PriorityQueue<>(comparator);
        ArrayList<Cell> rootList = new ArrayList<>();
        rootList.add(start);
        priorityQueue.add(rootList);
        System.out.println("before loop 1");
        while(!priorityQueue.isEmpty()){
            System.out.println("inside loop1");
            current = priorityQueue.poll();
            Cell latest = current.get(current.size()-1);
            latest.setVisited(true);
            //controller.setCellSearched(latest);
            //controller.setPathConsidering(current);
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
                latest.setVisited(true);
                //controller.setCellSearched(latest);
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
                //Thread.sleep(sleepDuration);
            }

            if (nextCells.size()>1){
                for (Cell c: nextCells) {
                    List<Cell> nextPath = new ArrayList<>(current);
                    nextPath.add(c);
                    priorityQueue.add(nextPath);
                }
                //Thread.sleep(sleepDuration);
            }

        }

        System.out.println("Number of Paths Found: "+foundPaths.size());
        if (foundPaths.isEmpty()){
            // no path is found.
        } else {
            for (int i=0; i<foundPaths.size(); i++){
                controller.setAllPathFound(foundPaths.poll());
            }
        }
    }
}
