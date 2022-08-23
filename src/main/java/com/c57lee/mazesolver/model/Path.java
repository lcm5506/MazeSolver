package com.c57lee.mazesolver.model;

import java.util.ArrayList;
import java.util.List;

public class Path {

    List<List<Cell>> path;

    public Path(){
        path = new ArrayList<>();
    }

    public void addEdge(List<Cell> edge){
        path.add(edge);
    }

    public void removeEdge(int index){
        path.remove(index);
    }

    public void replace(Path p){

    }

}
