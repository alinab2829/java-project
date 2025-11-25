package com.virus.view;

import com.virus.model.SimulationEngine;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;

import java.util.List;

public class ChartPanel extends VBox {
    private final LineChart<Number, Number> chart;
    private final XYChart.Series<Number, Number>[] series;
    private final NumberAxis xAxis;
    private final NumberAxis yAxis;

    public ChartPanel() {
        xAxis = new NumberAxis();
        xAxis.setLabel("Время (0 д.)");


        yAxis = new NumberAxis();
        yAxis.setLabel("Доля населения");

        chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("SEIR динамика");

        String [] names = {"Восприимчивые", "Носители", "Зараженные", "Выздоровевшие", "Умершие"};
        series = new XYChart.Series[5];
        for (int i = 0; i < 5; i++){
            series[i] = new XYChart.Series<>();
            series[i].setName(names[i]);
            chart.getData().add(series[i]);
        }
        getChildren().add(chart);
    }

    public void update(SimulationEngine engine){
        for (int i = 0; i < 5; i++){
            series[i].getData().clear();
        }

        List<double[]> history = engine.getHistory();
        for (double[] d : history){
            for (int i = 0; i < 5; i++){
                series[i].getData().add(new XYChart.Data<>(d[0], d[i+1]));
            }
        }

        double days = 0;
        if (!history.isEmpty()){
            double[] last = history.get(history.size() - 1);
            days = last[0];
        }

        xAxis.setLabel(String.format("Время (%.0f д.)", days));

    }
}
