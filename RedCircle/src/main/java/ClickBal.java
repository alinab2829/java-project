import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;


public class ClickBal extends Application{
    private boolean jumping = false;
    private double angle = 0;
    private double amplitude = 100;//высота прыжка
    private final double speed =0.05;//скорость анимации
    private       int index = 0;
    @Override
    public void start(Stage stage){
        Pane root = new Pane();
        //Создаем сцену
        Scene scene = new Scene(root,800,800,Color.SKYBLUE);


        Color[] colors={
                Color.RED,
                Color.VIOLET,
                Color.DARKOLIVEGREEN,
                Color.MEDIUMSPRINGGREEN,
                Color.BLUE,
                Color.CADETBLUE,
                Color.CORAL,
                Color.HOTPINK

        };

        Color[] colors2={
                Color.RED,
                Color.LIGHTGRAY,
                Color.SALMON,
                Color.AQUA,
                Color.BLACK,
                Color.BLUE,
                Color.CORAL,
                Color.HOTPINK

        };
        //Шар
        Circle ball = new Circle(30,Color.RED);
        ball.setCenterX(300);
        ball.setCenterY(750);

        root.getChildren().add(ball);

        Circle ball2 = new Circle(30,Color.RED);
        ball2.setCenterX(400);
        ball2.setCenterY(750);
        root.getChildren().add(ball2);



        //Анимация(запущенна постоянно но пргыет только при umping == true

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(!jumping)return;

                angle+= speed;

                //синусовый прыжок
                double y =600- Math.sin(angle)*amplitude;
                ball.setCenterY(y);
                ball2.setCenterY(y);
                //когда синусоида закончилась - прыжок завершен
                if(angle>=Math.PI){
                    jumping= false;
                    angle=0;
                    ball.setCenterY(750);
                    ball2.setCenterY(750);
                }
            }
        };
        timer.start();

        //реакция на клик
        ball.addEventFilter(MouseEvent.MOUSE_CLICKED, e->{
            if(!jumping){
                jumping=true;
                index+=1;
                amplitude=150+Math.random()*300;
                if(index>=colors.length){
                    index=0;
                }
                angle=0;
                ball.setFill(colors[index]);
                ball2.setFill(colors2[index]);
            }
        });
    //реакция на клик
        ball2.addEventFilter(MouseEvent.MOUSE_CLICKED, e->{
            if(!jumping){
                jumping=true;
                index+=1;
                amplitude=150+Math.random()*300;
                if(index>=colors2.length){
                    index=0;
                }
                angle=0;
                ball2.setFill(colors2[index]);
                ball.setFill(colors[index]);
            }
        });

        //движение по горизантали за мышкой
        scene .setOnMouseMoved(e->{
            double x = e.getX();
            //Ограничения по размеру чтобы шарик не вываливался за прежделы поля
            double radius = ball.getRadius();
            double radius2 = ball2.getRadius();

            if (x<radius) x =radius;
            if(x>scene.getWidth()-radius) x=scene.getWidth()- radius;
            if (x<radius2) x =radius2;
            if(x>scene.getWidth()-radius2) x=scene.getWidth()- radius2;
            ball2.setCenterX(x+100);
            ball.setCenterX(x);
        });

        stage.setTitle("Кликающий шар");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args){
        launch();
    }


}
