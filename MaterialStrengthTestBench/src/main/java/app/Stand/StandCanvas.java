package app.Stand;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

// StandCanvas — это кастомный компонент JavaFX, визуализирующий "тест давления трубы"
public class StandCanvas extends Canvas {


    private double pressure = 0;           // текущее давление в трубе
    private double maxPressure = 500;      // максимальное давление перед разрушением
    private boolean broken = false;        // флаг: труба сломана или нет
    private final DoubleProperty crackProgress = new SimpleDoubleProperty(0); // прогресс трещины (0..1)

    // Конструктор с размерами Canvas
    public StandCanvas(int width, int height) {
        super(width, height);
    }

    // Устанавливает текущее давление и сразу перерисовывает Canvas
    public void setPressure(double p) {
        this.pressure = p;
        draw();
    }

    // Сбрасывает состояние разрушения, прогресс трещины и перерисовывает
    public void resetBreak() {
        broken = false;
        crackProgress.set(0);
        draw();
    }

    // Устанавливает состояние разрушения и обновляет отображение
    public void setBroken(boolean b) {
        broken = b;
        draw();
    }

    // Проверяет, сломана ли труба
    public boolean isBroken() {
        return broken;
    }

    // Основной метод рисования трубы, жидкости и поршня
    public void draw() {
        GraphicsContext g = getGraphicsContext2D();
        g.clearRect(0, 0, getWidth(), getHeight()); // очищаем Canvas

        // Рисуем корпус трубы
        g.setFill(Color.LIGHTGRAY);
        g.fillRect(50, 80, 300, 60);

        // Рисуем жидкость внутри трубы
        double ratio = Math.min(1, pressure / maxPressure); // пропорция заполнения
        double fluidLen = 300 * ratio;                     // длина жидкости
        Color fluidColor = broken ? Color.RED : Color.BLUE;
        g.setFill(fluidColor);
        g.fillRect(50, 80, fluidLen, 60);

        // Рисуем поршень
        g.setFill(Color.DARKGRAY);
        g.fillRect(50 + fluidLen, 70, 20, 80);

        // Текст с текущим давлением
        g.setFill(Color.BLACK);
        g.fillText("Давление: " + (int) pressure, 50, 50);

        // Статус трубы
        if (broken) {
            g.setFill(Color.RED);
            g.fillText("ТРУБА РАЗРУШЕНА!", 50, 30);
        } else if (pressure >= maxPressure) {
            g.setFill(Color.GREEN);
            g.fillText("ТРУБА НЕ РАЗРУШЕНА!", 50, 30);
        }
    }


}
