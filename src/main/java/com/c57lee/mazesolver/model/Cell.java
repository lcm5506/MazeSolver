package com.c57lee.mazesolver.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cell extends Vertex{
    // top,bottom,left,right is boolean value representing open wall as true and closed wall as false.
    // i.e. if any is true, we can traverse from current cell to the neighboring cell through corresponding side.
    boolean top,bottom,left,right;
    boolean visited;

    public Cell(int x, int y){
        super(x,y);
        top = false;
        bottom = false;
        left = false;
        right = false;
        visited = false;
    }

    public boolean isTopOpen() {
        return top;
    }

    public boolean isBottomOpen() {
        return bottom;
    }

    public boolean isLeftOpen() {
        return left;
    }

    public boolean isRightOpen() {
        return right;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setTopOpen(boolean top) {
        this.top = top;
    }

    public void setBottomOpen(boolean bottom) {
        this.bottom = bottom;
    }

    public void setLeftOpen(boolean left) {
        this.left = left;
    }

    public void setRightOpen(boolean right) {
        this.right = right;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }


    @Override
    public String toString() {
        return "Cell{" +
                "top=" + top +
                ", bottom=" + bottom +
                ", left=" + left +
                ", right=" + right +
                ", x=" + x +
                ", y=" + y +
                ", visited=" + visited +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return top == cell.top && bottom == cell.bottom && left == cell.left && right == cell.right && x == cell.x && y == cell.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
