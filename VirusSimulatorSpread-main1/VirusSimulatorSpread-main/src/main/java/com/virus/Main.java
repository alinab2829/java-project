package com.virus;
import com.virus.controller.SimulationController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main  extends Application{
    @Override
    public void start (Stage stage){
        SimulationController controller = new SimulationController();
        BorderPane root = controller.getRoot();

        Scene scene = new Scene(root, 1200, 800);
        stage.setTitle("Симулятор распростронения вируса");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) { launch(args); }
}
