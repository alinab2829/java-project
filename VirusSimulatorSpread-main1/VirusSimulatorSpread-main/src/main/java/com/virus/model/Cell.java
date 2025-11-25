package com.virus.model;

public class Cell {
    public double S,E,I,R,D;
    public final int N = 100; //население ячейки

    public  Cell(){
        reset();

    }

    public void reset(){
        S = N;  //все здоровы
        I = 0;
        E = 0;
        R = 0;
        D = 0;
    }
    public int getTotalInfected(){ return (int) (E + I);}
    public int getTotalDead(){ return (int) D;}

}
