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
        return new Comparator<List<Cell>>() {
                    @Override
                    public int compare(List<Cell> o1, List<Cell> o2) {
                        Cell c1 = o1.get(o1.size()-1);
                        Cell c2 = o2.get(o2.size()-1);
                        double ab1 = Math.abs(c1.getX()- finish.getX());
                        double bc1 = Math.abs(c1.getY()- finish.getY());
                        double ab2 = Math.abs(c2.getX()- finish.getX());
                        double bc2 = Math.abs(c2.getY()- finish.getY());
//                        double weight1 = o1.size()+Math.hypot(ab1,bc1);
//                        double weight2 = o2.size()+Math.hypot(ab2,bc2);
                        double weight1 = Math.hypot(ab1,bc1);
                        double weight2 = Math.hypot(ab2,bc2);
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
