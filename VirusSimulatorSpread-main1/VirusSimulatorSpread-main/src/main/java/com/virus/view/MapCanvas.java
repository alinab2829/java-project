package com.virus.view;

import com.virus.model.Cell;
import com.virus.model.PopulationGrid;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;


public class MapCanvas extends Pane {
    private final Canvas canvas;
    private final int cellSize;

    public MapCanvas(int gridSize) {
        this.cellSize = 300/gridSize;
        canvas = new Canvas(300, 300);

        getChildren().add(canvas);

    }

    public void draw (PopulationGrid grid){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, 600, 600);

        for (int i = 0; i < grid.getSize(); i++){
            for (int j = 0; j < grid.getSize(); j++){
                Cell c = grid.get(i, j);
                double inf = (c.E + c.I) / 100;
                double dead = c.D / 100;

                Color color = Color.GREEN;

                if (dead > 0.5) color = Color.BLACK;
                else if (inf > 0.7) color = Color.RED;
                else if (inf > 0.3) color = Color.ORANGE;
                else if (inf > 0) color = Color.YELLOW;

                gc.setFill(color);
                gc.fillRect(i * cellSize, j * cellSize, cellSize, cellSize);
            }
        }
    }


}
