package com.virus.view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class ColorLegend extends HBox {

    public ColorLegend(){
        setSpacing(10);
        setPadding(new Insets(7));

        getChildren().addAll(
                item(Color.GREEN, "Нет инфекции"),
                item(Color.YELLOW, "Ранее зараженные"),
                item(Color.ORANGE, "Среднее заражение"),
                item(Color.RED, "Сильная вспышка"),
                item(Color.BLACK, "Всокая смертность")
        );
    }
    private HBox item(Color color, String text){
        Rectangle rect = new Rectangle(20, 20, color);
        Label label = new Label(text);
        HBox box = new HBox(5, rect, label);
        return box;
    }
}
