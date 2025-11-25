package com.virus.model;

import java.util.ArrayList;
import java.util.List;

public class SimulationEngine {
    private final PopulationGrid grid;
    private final Parameters params;
    private final List<double[]> history = new ArrayList<>();
    private double time = 0;

    public SimulationEngine(int size, Parameters params) {
        this.grid = new PopulationGrid(size);
        this.params = params;
        record ();
    }

    public void update (double dt){
        time += dt;
        int n = grid.getSize();

        double beta = params.getBeta();
        double sigma = params.getSigma();
        double gammaRec = params.getRecoveryRate();
        double gammaDeath = params.getDeathRate();
        double vacc = params.getVaccineRate() / 100.0; // 0..1 доля в день
        double neighK = params.getNeighborFactor();
        boolean useSEIR = params.isUseSEIR();

        // Временная буферизация

        double [][] dS = new double[n][n];
        double [][] dE = new double[n][n];
        double [][] dI = new double[n][n];
        double [][] dR = new double[n][n];
        double [][] dD = new double[n][n];


        // 1. Локальная динамика: вакцинация, заражение, переходы E -> I, I -> R, D

        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                Cell c = grid.get(i, j);

                double S = c.S;
                double E = c.E;
                double I = c.I;

                // Вакцинация: часть S -> R
                if (vacc > 0 && S > 0){
                    double vacFlow = S * vacc * dt;
                    if (vacFlow > S) vacFlow = S;

                    dS[i][j] -= vacFlow;
                    dR[i][j] += vacFlow;


                    S -= vacFlow; // не заражаются вакцинированные
                }

                // Локальное заражение внутри ячейки: S -> E или S -> I (если SIR)
                if (I > 0 && S > 0 && beta > 0){
                    double  lambda = beta * I / c.N; // сила инфекция
                    double newExp = lambda * S * dt;
                    if (newExp > S) newExp = S;

                    if (useSEIR) {
                        dS[i][j] -= newExp;
                        dE[i][j] += newExp;
                    }else {
                        dS[i][j] -= newExp;
                        dI[i][j] += newExp;
                    }
                }

                // Переход E -> I (SEIR активен)

                if (useSEIR && E > 0 && sigma > 0){
                    double eToI = sigma * E * dt;
                    if (eToI > E) eToI = E;

                    dE[i][j] -= eToI;
                    dE[i][j] += eToI;
                }

                // Переходы I -> R и I -> D

                if (I > 0 && (gammaRec > 0 || gammaDeath > 0)){
                    double recFlow = gammaRec * I * dt;
                    double deathFlow = gammaDeath * I * dt;
                    double totalOut = recFlow + deathFlow;

                    // защита от "ухода" больше, чем было в I

                    if (totalOut > I){
                        double factor = I / totalOut;
                        recFlow *= factor;
                        deathFlow *= factor;
                    }

                    dI[i][j] -= (recFlow + deathFlow);
                    dR[i][j] += recFlow;
                    dD[i][j] += deathFlow;
                }
            }
        }
        // 2. Заражение соседних ячеек

        int [][] dirs = {{0,1}, {1, 0}, {0, -1}, {-1, 0}};

        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                Cell src = grid.get(i, j);
                if (src.I <= 0)continue;

                for (int[] d : dirs){
                    int ni = i + d[0];
                    int nj = j + d[1];
                    if (ni < 0 || ni >= n || nj < 0 || nj >= n) continue;

                    Cell dst = grid.get(ni, nj);
                    if (dst.S <= 0) continue;

                    double Sdst = dst.S;
                    double lambdaNeigh = beta * neighK * src.I / src.N;
                    double newExpN = lambdaNeigh * Sdst * dt;

                    if (newExpN > Sdst) newExpN = Sdst;

                    if (useSEIR) {
                        dS[ni][nj] -= newExpN;
                        dE[ni][nj] += newExpN;
                    } else{
                        dS[ni][nj] -= newExpN;
                        dI[ni][nj] += newExpN;
                    }

                }
            }
        }
        // Применяем превращение
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                Cell c = grid.get(i, j);
                c.S += dS[i][j];
                c.E += dE[i][j];
                c.I += dI[i][j];
                c.R += dR[i][j];
                c.D += dD[i][j];

                // удалить "мусор"
            }
        }
    }

    private void record() {
        double S = 0, E =0, I = 0, R = 0, D = 0;
        int size = grid.getSize();
        int total = size * size * 100; // N = 100 в каждой клетке

        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++){
                Cell c = grid.get(i, j);

                S += c.S;
                E += c.E;
                I += c.I;
                R += c.R;
                D += c.D;
            }
            history.add(new double[] {time, S/total, E/total, I/total, R/total, D/total});

        }
    }
    public PopulationGrid getGrid(){return grid;}

    public List<double[]> getHistory() {return history;}

    public void reset(){
        grid.reset();
        history.clear();
        time = 0;
        record();
    }

    public double getTime() {return time;}


}
