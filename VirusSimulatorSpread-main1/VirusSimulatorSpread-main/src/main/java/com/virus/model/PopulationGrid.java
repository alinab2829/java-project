package com.virus.model;


public class PopulationGrid {
    private final Cell [][] grid;
    private final int size;

    public PopulationGrid( int size){
        this.size = size;
        grid = new Cell[size][size];
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                grid[i][j] = new Cell(); // все здоровы
            }
        }

        // "нулевой пациент" - одна зараженная ячейка в центре карты
        int cx = size/2;
        int cy = size/2;
        Cell c = grid[cx][cy];
        c.S -= 1;
        c.I += 1;
    }
    public Cell get(int x, int y) {return grid[x][y];}
    public int getSize(){return size;}

    public void reset () {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j].reset(); // все здоровы
            }
        }
        // И снова "нулевой пациент"
        int cx = size/2;
        int cy = size/2;
        Cell c = grid[cx][cy];
        c.S -= 1;
        c.I += 1;
    }
}
