package com.c57lee.mazesolver.pathfind;

import com.c57lee.mazesolver.model.Cell;
import com.c57lee.mazesolver.model.Maze;
import com.c57lee.mazesolver.model.Vertex;
import javafx.concurrent.Task;


import java.util.*;

public class Dijkstra extends PathFind{

    HashMap<Cell,Integer> map;  // map contains length of current shortest path to the key 'Cell'
                                // map is used to determine, which path to a specific vertex A is shorter path,
                                // when we have more than one path to the vertex A.
    public Dijkstra(Maze maze) {
        super(maze);
        map = new HashMap<>();
    }

    @Override
    public Task<List<Cell>> getTask() {
        return new Task<List<Cell>>() {
            @Override
            protected List<Cell> call() throws Exception {
                // Setup
                Comparator<List<Cell>> comparator = getComparator();
                PriorityQueue<List<Cell>> priorityQueue = new PriorityQueue<>(comparator);
                PriorityQueue<List<Cell>> foundPaths = new PriorityQueue<>(Comparator.comparingInt(List::size));
                ArrayList<Cell> rootList = new ArrayList<>();

                // Add root to the priority queue as a starting point
                rootList.add(start);
                priorityQueue.add(rootList);
                int minLength = Integer.MAX_VALUE;

                while(!priorityQueue.isEmpty()){
                    List <Cell> current = priorityQueue.poll();

                    Cell latest = current.get(current.size()-1);
                    controller.setCellSearched(latest); // Notify the ui of the cell currently being looked at by the algorithm

                    // Drop current paths in consideration that have no chance of being shorter than 'found' path
                    // IF we found a path to destination. minLength starts with Integer.MAX_VALUE at the beginning.
                    // Thus this is ignored before we find at least one path to 'finish'.
                    // This is unnecessary since all mazes generated in this program are perfect maze.
                    // This is usefully when we have imperfect mazes.
                    if ((current.size()+getDistance(latest,finish))>minLength)
                        continue;

                    Cell previous = null;
                    if (current.size()>1)
                        previous = current.get(current.size()-2);

                    List<Cell> nextCells = maze.getOpenNeighbors(latest);
                    nextCells.remove(previous);
                    nextCells.removeAll(current);

                    if (nextCells.isEmpty())
                        continue;
                    for(Cell next: nextCells){
                        List<Cell> nextPath = new ArrayList<>(current);
                        List<Cell> edge = getEdge(latest,next);

                        // 'finish' is found.
                        if (edge.contains(finish)){
                            nextPath.addAll(edge.subList(0,edge.indexOf(finish)+1));
                            foundPaths.add(nextPath);
                            controller.setAllPathFound(nextPath); // This is in case we have multiple paths to finish.
                            System.out.println("path found!");
                            if (nextPath.size() < minLength)
                                minLength = nextPath.size();        // set minLength so that the algorithm does not search entire maze afterwards,
                            return nextPath;                        // looking for better alternate path.
                                                                    // replace return statement with continue in case of imperfect mazes and general path finding.
                        }

                        nextPath.addAll(edge);
                        int isInMap = isInMap(nextPath); // Check if we found shorter path to the vertex 'latest'
                        // If we did find a shorter path, we replace previously optimal path  in priorityQueue with newly found optimal path
                        if (isInMap == 1){
                            priorityQueue.removeIf(l->l.contains(nextPath.get(nextPath.size()-1)));
                            priorityQueue.add(nextPath);
                            map.replace(nextPath.get(nextPath.size()-1),nextPath.size());
                        } else if (isInMap == -1) {
                            priorityQueue.add(nextPath);
                            map.put(nextPath.get(nextPath.size()-1),nextPath.size());
                        }
                        // Ignore 'nextPath' if we already have more optimal path to 'latest' in priorityQueue.
                    }
                }

                System.out.println("Number of Paths Found: "+foundPaths.size());
                return foundPaths.poll();
            }
        };
    }

    // isInMap determines
    // Given a 'path', return -1 IF map does not have a path to the vertex 'latest' (the last node of the 'path')
    //                 return  0 IF map contains shorter path to the vertex 'latest'
    //                 return  1 IF 'path' is shorter or more optimal than the stored path to the vertex 'latest'
    public int isInMap(List<Cell> path){
        Cell latest = path.get(path.size()-1);
        if (!map.containsKey(latest))
            return -1;
        if (map.get(latest)<=path.size())
            return 0;
        return 1;
    }

    // Comparator for Dijkstra compares two paths by the length of the path.
    // Path with shortest length gets searched first.
    public Comparator<List<Cell>> getComparator(){
        return Comparator.comparingInt(List::size);
    }
    public double getDistance(Cell c1, Cell c2){
        double ab = Math.abs(c1.getX()-c2.getX());
        double bc = Math.abs(c1.getY()-c2.getY());
        return Math.hypot(ab,bc);
    }

}
