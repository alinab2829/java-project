package app.Material;

import java.io.Serializable;

// Класс Material описывает материал с названием и пределом прочности
// Реализует интерфейс Serializable для возможности сериализации объектов
public class Material implements Serializable {


    private String name;       // название материала
    private double strength;   // предел прочности в Па (σ_max)

    // Пустой конструктор нужен, например, для корректной работы с Gson (для десериализации)
    public Material() {
    }

    // Конструктор с параметрами
    public Material(String name, double strength) {
        this.name = name;
        this.strength = strength;
    }

    // Геттер для имени материала
    public String getName() {
        return name;
    }

    // Геттер для предела прочности
    public double getStrength() {
        return strength;
    }

    // Переопределяем метод toString для удобного отображения объекта
    @Override
    public String toString() {
        return name + " (σ_max=" + strength + " Па)";
    }

}
