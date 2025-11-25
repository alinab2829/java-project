package com.virus.controller;

import com.virus.model.Parameters;
import com.virus.model.PopulationGrid;
import com.virus.model.SimulationEngine;
import com.virus.view.ChartPanel;
import com.virus.view.ColorLegend;
import com.virus.view.ControlPanel;
import com.virus.view.MapCanvas;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SimulationController {
    private final Parameters params;
    private final SimulationEngine engine;
    private final MapCanvas map;
    private final ChartPanel chart;
    private final ControlPanel controls;
    private final AnimationTimer timer;
    private boolean running = false;
    private final double dt = 1.0; // шаг модели в днях

    private BorderPane root;
    public BorderPane getRoot() {return root;}


    public SimulationController (){
        this.params = new Parameters();
        this.engine = new SimulationEngine(50, params);
        this.map = new MapCanvas(50);
        this.chart = new ChartPanel();
        this.controls = new ControlPanel(params, this::reset);

        initView();
        this.timer = initTimer();
        updateView();
    }

    private void initView() {

        // Кнопки управления
        Button start = new Button("СТАРТ");
        Button reset = new Button("СБРОС");
        Button export = new Button("ЭКСПОРТ");
        Button help = new Button("СПРАВКА");

        start.setOnAction(e -> toogle());
        reset.setOnAction(e -> reset());
        export.setOnAction(e -> exportCSV());
        help.setOnAction(e -> showHelp());

        HBox buttons = new HBox(10, start, reset, export, help);
        buttons.setStyle("-fx-padding: 10;");

        //  Карта + легенда под ней
        ColorLegend legend = new ColorLegend();
        VBox centerBox = new VBox(10, map, legend);

        BorderPane root = new BorderPane();
        root.setTop(buttons);
        root.setCenter(centerBox);
        root.setBottom(chart);
        root.setRight(controls);

        this.root = root;

    }

    private void exportCSV() {
    }

    private AnimationTimer initTimer() {
        return new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (running){
                    engine.update(dt);
                    updateView();

                    // ограничения по времени моделирования
                    double maxDays = params.getMaxDays();
                    if (engine.getTime() >= maxDays){
                        running = false;
                        stop();
                    }
                }
            }
        };
    }

    private void updateView() {
        map.draw(engine.getGrid());
        chart.update(engine);
    }

    private void toogle() {
        running = !running;
        if (running){
            timer.start();
        }else {
            timer.stop();
        }
    }

    private void reset() {
        running = false;
        timer.stop();
        engine.reset();
        updateView();
    }

    private void showHelp() {
        String text =
                "ПОМОЩЬ";
    }
}
