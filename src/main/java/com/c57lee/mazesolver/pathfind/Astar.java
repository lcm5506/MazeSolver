package com.c57lee.mazesolver.pathfind;

import com.c57lee.mazesolver.model.Cell;
import com.c57lee.mazesolver.model.Maze;
import javafx.concurrent.Task;

import java.util.*;

public class Astar extends Dijkstra{
    HashMap<Cell,List<Cell>> map;
    public Astar(Maze maze) {
        super(maze);
        map = new HashMap<>();
    }

    @Override
    public Comparator<List<Cell>> getComparator(){

        // Astar is just a variation of Dijkstra's algorithm. Instead of prioritizing paths with shortest length,
        // prioritize paths that are heading towards 'finish'.
        return new Comparator<List<Cell>>() {
                    @Override
                    public int compare(List<Cell> o1, List<Cell> o2) {
                        Cell c1 = o1.get(o1.size()-1);
                        Cell c2 = o2.get(o2.size()-1);
//                        double weight1 = o1.size() + getDistance(c1,finish); // This is commented out to display more dramatized difference
//                        double weight2 = o2.size() + getDistance(c2,finish); // between Dijkstra and Astar.
                        double weight1 = getDistance(c1,finish);
                        double weight2 = getDistance(c2,finish);
                        if (weight1>weight2)
                            return 1;
                        else if (weight1<weight2)
                            return -1;
                        else
                            return 0;
                    }
                };
    }

}
