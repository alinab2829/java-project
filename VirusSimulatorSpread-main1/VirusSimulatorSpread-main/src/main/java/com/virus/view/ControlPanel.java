package com.virus.view;

import com.virus.model.Parameters;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ControlPanel extends VBox {
    private final Parameters params;
    private final Slider r0Slider, incSlider, recSlider,
            mortSlider, vacSlider, maskSlider, maxDaysSlider;
    private final CheckBox seirCheck;
    private final Label r0Label = new Label();

    public ControlPanel(Parameters params, Runnable onChange){
        this.params = params;
        setSpacing(10);
        setPadding(new Insets(15));
        setPrefWidth(560); // ширина панели

        // Заголовок панели

        Label title = new Label("Параметры модели");
        title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        /**
         * Слайдеры
         * */
        r0Slider = slider(0.5, 6, params.getR0(), 0.1);
        incSlider = slider(1, 14, params.getIncubation(), 1);
        recSlider = slider(3, 30, params.getRecovery(), 1);
        mortSlider = slider(0, 1, params.getMortality(), 0.1);
        maskSlider = slider(0, 0.8, params.getMaskEffect(), 0.05);
        vacSlider = slider(0, 5, params.getVaccineRate(), 0.1);
        maxDaysSlider = slider(1, 365, params.getMaxDays(), 1);

        seirCheck = new CheckBox("Использовать SEIR (с инкубационным периодом)");
        seirCheck.setSelected(true);

        // ---- СЛУШАТЕЛИ ----
        r0Slider.valueProperty().addListener((o, ov, nv) -> {
            params.setR0(nv.doubleValue());
            r0Label.setText("R₀: " + String.format("%2f", nv.doubleValue()));
            onChange.run();
        });

        incSlider.valueProperty().addListener((o, ov, nv) -> {
            params.setIncubation(nv.doubleValue()); onChange.run(); });
        recSlider.valueProperty().addListener((o, ov, nv) -> {
            params.setRecovery(nv.doubleValue()); onChange.run();});
        mortSlider.valueProperty().addListener((o, ov, nv) -> {
            params.setMortality(nv.doubleValue()); onChange.run(); });
        maskSlider.valueProperty().addListener((o, ov, nv) -> {
            params.setMaskEffect(nv.doubleValue()); onChange.run();});
        vacSlider.valueProperty().addListener((o, ov, nv) -> {
            params.setVaccineRate(nv.doubleValue()); onChange.run();});
        maxDaysSlider.valueProperty().addListener((o, ov, nv) -> {
            params.setMaxDays(nv.doubleValue()); onChange.run();});

        seirCheck.selectedProperty().addListener((o, ov, nv) -> {
            params.setUseSEIR(nv);
            onChange.run(); });

        // --- СЕТКА ПАРАМЕТРОВ ---

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);

        // Колонки: подпись справа

        ColumnConstraints colLabel = new ColumnConstraints();
        colLabel.setMinWidth(150);
        colLabel.setHalignment(HPos.RIGHT);

        ColumnConstraints colControl = new ColumnConstraints();
        colControl.setHgrow(Priority.ALWAYS);

        grid.getColumnConstraints().addAll(colLabel, colControl);

        int row = 0;
        grid.add(r0Label, 0, row); grid.add(r0Slider, 1, row++);
        grid.add(new Label("Инкубационный период (дни): "), 0, row);
        grid.add(incSlider, 1, row++);
        grid.add(new Label("Период выздоровления (дни): "), 0, row);
        grid.add(recSlider, 1, row++);
        grid.add(new Label("Летальность (0-1, доля): "), 0, row);
        grid.add(mortSlider, 1, row++);
        grid.add(new Label("Эффективность масок: "), 0, row);
        grid.add(maskSlider, 1, row++);
        grid.add(new Label("Вакцинация: "), 0, row);
        grid.add(vacSlider, 1, row++);
        grid.add(new Label("Максимальное время: "), 0, row);
        grid.add(maxDaysSlider, 1, row++);
        grid.add(seirCheck, 0, row, 2, 1);

        getChildren().addAll(title, grid);
    }
    /***Унифицированный слайдер*/
    private Slider slider(double min, double max, double val, double step) {
        Slider s = new Slider(min, max, val);
        s.setShowTickLabels(true);
        s.setShowTickMarks(true);
        s.setMajorTickUnit((max-min)/5.0);
        s.setBlockIncrement(step);

        s.setPrefWidth(330);
        s.setMaxWidth(Double.MAX_VALUE);

        return s;
    }
}