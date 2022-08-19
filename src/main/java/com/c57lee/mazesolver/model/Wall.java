package com.c57lee.mazesolver.model;

public class Wall {

    private Cell cell1;
    private Cell cell2;

    public Wall(Cell cell1, Cell cell2) {
        // Must check if cell1 and cell2 is 'neighboring' cells.
        int deltaX = cell1.getX()-cell2.getX();
        int deltaY = cell1.getY()-cell2.getY();
        if (Math.abs(deltaX) + Math.abs(deltaY) == 1) {
            this.cell1 = cell1;
            this.cell2 = cell2;
        } else throw new IllegalArgumentException("cell1 and cell2 are not neighboring cells.");
    }

    public Cell getCell1(){
        return cell1;
    }

    public Cell getCell2(){
        return cell2;
    }


    @Override
    public String toString() {
        return "Wall{" +
                "cell1=" + cell1 +
                ", cell2=" + cell2 +
                '}';
    }
}
