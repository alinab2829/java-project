package app.Material;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

// Класс Manometer наследуется от Canvas и реализует простую аналоговую шкалу давления
public class Manometer extends Canvas {


    private double pressure = 0;      // текущее давление
    private double maxPressure = 100; // максимальное давление для полного оборота стрелки

    // Конструктор с параметром size (размер холста)
    public Manometer(int size) {
        super(size, size); // задаём ширину и высоту Canvas
        draw();            // сразу рисуем шкалу при создании объекта
    }

    // Метод для установки текущего давления
    public void setPressure(double p) {
        this.pressure = p;
        draw(); // перерисовываем шкалу при изменении давления
    }

    // Метод для установки максимального давления
    public void setMaxPressure(double max) {
        this.maxPressure = max;
        draw(); // перерисовываем шкалу при изменении максимума
    }

    // Основной метод рисования манометра
    private void draw() {
        GraphicsContext g = getGraphicsContext2D(); // получаем контекст для рисования
        double w = getWidth();
        double h = getHeight();
        double radius = Math.min(w, h) / 2 - 10; // радиус круга с отступом 10px

        g.clearRect(0, 0, w, h); // очищаем холст перед перерисовкой

        double cx = w / 2; // центр по X
        double cy = h / 2; // центр по Y

        // Рисуем внешнюю окружность манометра
        g.setStroke(Color.BLACK);
        g.setLineWidth(4);
        g.strokeOval(cx - radius, cy - radius, radius * 2, radius * 2);

        // Длина стрелки — 70% радиуса
        double arrowLength = radius * 0.7;

        // Вычисляем угол стрелки
        double angle;
        if (pressure <= maxPressure) {
            angle = 90 + (pressure / maxPressure) * 360; // один полный оборот по мере увеличения давления
        } else {
            angle = 450; // если давление больше максимума, стрелка остаётся на конце круга
        }

        // Вычисляем координаты конца стрелки
        double x = cx + arrowLength * Math.cos(Math.toRadians(angle));
        double y = cy + arrowLength * Math.sin(Math.toRadians(angle));

        // Рисуем стрелку
        g.setStroke(Color.RED);
        g.setLineWidth(3);
        g.strokeLine(cx, cy, x, y);

        // Рисуем текст с текущим давлением под стрелкой
        String text = "P: " + (int) pressure;
        Text t = new Text(text);
        t.setFont(new Font(16));
        double textWidth = t.getLayoutBounds().getWidth();

        // Центрируем текст под стрелкой
        g.fillText(text, cx - textWidth / 2, cy + radius - 10);
    }


}
