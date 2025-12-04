package app.Controller;

import app.Material.Manometer;
import app.Material.Material;
import app.Stand.StandCanvas;
import app.Storage.MaterialStorage;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

// Контроллер для главного окна приложения: управляет UI, материалами и тестами
public class MainController {


    @FXML
    private VBox centerPane;

    @FXML
    private ComboBox<Material> materialBox;

    @FXML
    private Button startBtn;

    @FXML
    private Button addMaterialBtn;

    @FXML
    private Button clearMaterialsBtn;

    @FXML
    private Button setPressureBtn;

    @FXML
    private Button resetBtn;

    @FXML
    private StandCanvas canvas;

    @FXML
    private VBox rightPanel;

    private double pressure = 0;       // текущее давление в ходе теста
    private double testPressure = -1;  // давление, установленное пользователем

    private AnimationTimer timer;      // таймер для анимации повышения давления
    private Manometer manometer;       // визуальный манометр

    // Инициализация контроллера после загрузки FXML
    public void initialize() {
        materialBox.setPromptText("Материалы");

        // Создаём холст для визуализации станка
        canvas = new StandCanvas(400, 200);
        centerPane.getChildren().add(0, canvas);

        // Создаём манометр справа
        manometer = new Manometer(150);
        manometer.setMaxPressure(100);
        rightPanel.getChildren().add(manometer);

        // Привязываем кнопки к обработчикам
        startBtn.setOnAction(e -> startTest());
        addMaterialBtn.setOnAction(e -> addMaterial());
        clearMaterialsBtn.setOnAction(e -> clearMaterials());
        setPressureBtn.setOnAction(e -> setPressure());
        materialBox.setOnAction(e -> selectMaterial());
        resetBtn.setOnAction(e -> resetAll());

        setupAnimation(); // настройка анимации

        // Загружаем материалы из хранилища
        materialBox.getItems().addAll(MaterialStorage.load());
    }

    // Настройка AnimationTimer для плавного повышения давления
    private void setupAnimation() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Material m = materialBox.getValue();
                if (m == null || testPressure <= 0) return;

                // Плавное увеличение давления
                if (pressure < testPressure) {
                    pressure += 0.5; // скорость нарастания давления
                    if (pressure > testPressure) pressure = testPressure;

                    // Обновляем манометр и холст
                    manometer.setPressure(pressure);
                    canvas.setPressure(pressure);
                }

                // Проверка разрушения материала
                if (pressure >= m.getStrength() && !canvas.isBroken()) {
                    canvas.setBroken(true);
                    stop();
                    Platform.runLater(() -> showResult(false, m, pressure));
                    return;
                }

                // Если достигли testPressure и материал не разрушился
                if (pressure >= testPressure && !canvas.isBroken()) {
                    stop();
                    Platform.runLater(() -> showResult(true, m, pressure));
                }
            }
        };
    }

    // Запуск теста материала
    private void startTest() {
        Material m = materialBox.getValue();
        if (m == null) {
            showError("Материал не выбран", "Выберите или добавьте материал.");
            return;
        }
        if (testPressure <= 0) {
            showError("Ошибка", "Сначала задайте давление!");
            return;
        }
        canvas.resetBreak();
        pressure = 0;
        manometer.setPressure(0);
        timer.start();
    }

    // Установка давления через диалог
    private void setPressure() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Введите давление:");

        String value = dialog.showAndWait().orElse("").trim();

        if (value.isEmpty()) {
            showError("Ошибка", "Значение давления не может быть пустым!");
            return;
        }
        try {
            double newPressure = Double.parseDouble(value);
            if (newPressure <= 0) {
                showError("Ошибка", "Давление должно быть > 0!");
                return;
            }

            testPressure = newPressure;
            pressure = 0;
            canvas.resetBreak();
            manometer.setPressure(0);

        } catch (NumberFormatException e) {
            showError("Ошибка", "Введите корректное число!");
        }
    }

    // Добавление нового материала через диалоги
    private void addMaterial() {
        try {
            TextInputDialog nameDialog = new TextInputDialog();
            nameDialog.setHeaderText("Введите название материала:");
            String name = nameDialog.showAndWait().orElse("").trim();
            if (name.isEmpty()) {
                showError("Ошибка", "Название не может быть пустым!");
                return;
            }

            TextInputDialog strengthDialog = new TextInputDialog();
            strengthDialog.setHeaderText("Введите предел прочности (Па):");
            double strength = Double.parseDouble(strengthDialog.showAndWait().orElse("").trim());
            if (strength <= 0) {
                showError("Ошибка", "Предел прочности должен быть > 0!");
                return;
            }

            Material m = new Material(name, strength);
            materialBox.getItems().add(m);
            materialBox.setValue(m);
            MaterialStorage.add(m);

        } catch (Exception e) {
            showError("Ошибка", "Введите корректное число!");
        }
    }

    // Сброс давления при выборе нового материала
    private void selectMaterial() {
        pressure = 0;
        canvas.resetBreak();
        manometer.setPressure(0);
    }

    // Отображение результата испытания
    private void showResult(boolean ok, Material m, double pressure) {
        Alert alert = new Alert(ok ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle("Результат испытания");
        alert.setHeaderText(null);
        if (ok) {
            alert.setContentText("Материал выдержал испытание!");
        } else {
            alert.setContentText("Материал \"" + m.getName() + "\" разрушился при давлении " + (int) pressure);
        }
        alert.showAndWait();
    }

    // Универсальный метод для отображения ошибок
    private void showError(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }

    // Очистка всех материалов из ComboBox и JSON
    private void clearMaterials() {
        materialBox.getItems().clear();
        MaterialStorage.clear();
    }

    // Полный сброс состояния приложения
    private void resetAll() {
        if (timer != null) timer.stop();
        pressure = 0;
        testPressure = -1;

        if (manometer != null) manometer.setPressure(0);
        if (canvas != null) canvas.resetBreak();
        if (canvas != null) canvas.setPressure(0);

        if (materialBox != null) materialBox.getSelectionModel().clearSelection();
    }


}
