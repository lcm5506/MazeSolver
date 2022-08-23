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
                Comparator<List<Cell>> comparator = getComparator();
                PriorityQueue<List<Cell>> priorityQueue = new PriorityQueue<>(comparator);
                PriorityQueue<List<Cell>> foundPaths = new PriorityQueue<>(Comparator.comparingInt(List::size));
                ArrayList<Cell> rootList = new ArrayList<>();
                rootList.add(start);
                priorityQueue.add(rootList);
                int minLength = Integer.MAX_VALUE;

                while(!priorityQueue.isEmpty()){
                    List <Cell> current = priorityQueue.poll();

                    Cell latest = current.get(current.size()-1);
                    controller.setCellSearched(latest);

                    // Drop all paths in consideration that have no chance of being shorter than 'found' path.
                    if (current.size()+getDistance(latest,finish)>minLength)
                        continue;

                    Cell previous = null;
                    if (current.size()>1)
                        previous = current.get(current.size()-2);

                    List<Cell> nextCells = maze.getOpenNeighbors(latest);
                    nextCells.remove(previous);
                    nextCells.removeAll(current);

                    if (nextCells.isEmpty())
                        continue;
                    for(Cell c: nextCells){
                        List<Cell> nextPath = new ArrayList<>(current);
                        List<Cell> edge = getEdge(latest,c);
                        if (edge.contains(finish)){
                            nextPath.addAll(edge.subList(0,edge.indexOf(finish)+1));
                            foundPaths.add(nextPath);
                            controller.setAllPathFound(nextPath);
                            System.out.println("path found!");
                            if (nextPath.size() < minLength)
                                minLength = nextPath.size();
                            continue;
                        }
                        nextPath.addAll(getEdge(latest,c));
                        int isInMap = isInMap(nextPath);
                        if (isInMap == 1){
                            priorityQueue.removeIf(l->l.contains(nextPath.get(nextPath.size()-1)));
                            priorityQueue.add(nextPath);
                            map.replace(nextPath.get(nextPath.size()-1),nextPath.size());
                        } else if (isInMap == -1) {
                            priorityQueue.add(nextPath);
                            map.put(nextPath.get(nextPath.size()-1),nextPath.size());
                        }
                    }
                }

                System.out.println("Number of Paths Found: "+foundPaths.size());
                return foundPaths.poll();
            }
        };
    }

    public int isInMap(List<Cell> path){
        Cell latest = path.get(path.size()-1);
        if (!map.containsKey(latest))
            return -1;
        if (map.get(latest)<=path.size())
            return 0;
        return 1;
    }

    public Comparator<List<Cell>> getComparator(){
        return Comparator.comparingInt(List::size);
    }
    public double getDistance(Cell c1, Cell c2){
        double ab = Math.abs(c1.getX()-c2.getX());
        double bc = Math.abs(c1.getY()-c2.getY());
        return Math.hypot(ab,bc);
    }

}
