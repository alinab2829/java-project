import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ColorWindow extends Application {

    @Override
    public void start(Stage stage){
        //Корневой контейнер
        StackPane root = new StackPane();
        //Создаем сцену
        Scene scene = new Scene(root,400,300);

        //Массив цветов

        Color[] colors={
                Color.RED,
                Color.ORANGE,
                Color.YELLOW,
                Color.GREEN,
                Color.BLUE,
                Color.PURPLE

        };



        //Анимация - каждые 1 секунду меняет цвет
        Timeline timeline = new Timeline();

        for(int i=0;i<colors.length; i++){
            int index = i;

            //Каждый кадр наступает i секунду

            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(i),
                            e->root.setStyle("-fx-background-color: "+
                                    toHex(colors[index])+";"))
            );
        }


        //повторяем анимацию бесконечно
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        //Готово -показываем окно
        stage.setScene(scene);
        stage.setTitle("Цветовое окно");
        stage.show();
    }

    private String toHex(Color c) {
        return "#" +c.toString().substring(2,8);
    }

    public static void main(String[] args){
        launch();
    }



}
