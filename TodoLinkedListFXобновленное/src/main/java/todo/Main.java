package todo;
// все что начинается с Javafx то это для графического дизайна
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.stream.Collectors;
//создание кнопок окон список задач
public class Main extends Application{
    private final LinkedTasks model  = new LinkedTasks();//model - хранит все данные
    private final ObservableList<String> uiItems = FXCollections.observableArrayList();

    private ListView<String> listView;
    private TextField input;
    private Label status;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Листочек");
        input = new TextField();
        input.setPromptText("Новая задачка...");
        Button addBtn = new Button("Добавить");
        addBtn.setDefaultButton(true);

        HBox inputBox = new HBox(8, input, addBtn);
        inputBox.setAlignment(Pos.CENTER_LEFT);

        listView = new ListView<>(uiItems);
        listView.setPrefHeight(420);

        Button doneBtn = new Button("Готово");
        Button delBtn = new Button("Удалить");
        Button upBtn = new Button("⬆");
        Button dnBtn = new Button("⬇");
        Button clrBtn = new Button("Очистить");

        HBox action = new HBox(8, doneBtn, delBtn, upBtn, dnBtn, clrBtn);
        action.setAlignment(Pos.CENTER_LEFT);

        status = new Label("Готово");
        VBox root = new VBox(12, new Label("Список задач"), inputBox, listView, action, status);
        root.setPadding(new Insets(16));

//        обработчики
        addBtn.setOnAction(e-> addTask());
        root.setOnKeyPressed(e -> {if (e.getCode()== KeyCode.ENTER) addTask();});

        doneBtn.setOnAction(e-> {
           int i = listView.getSelectionModel().getSelectedIndex();
           model.toogleAt(i);
           syncUi();
        });

        delBtn.setOnAction(e->{
            int i = listView.getSelectionModel().getSelectedIndex();
            model.removeAt(i);
            syncUi();
        });

        upBtn.setOnAction(e -> move(-1));
        dnBtn.setOnAction(e -> move(+1));
        clrBtn.setOnAction(e -> {model.clean(); syncUi();});

//        Демо данные
        model.addLast("Новая задача");
        syncUi();

        stage.setScene(new Scene(root, 640, 520));
        stage.show();


    }

    private void addTask() {
        String text = input.getText().trim();
        if(text.isEmpty()){status.setText("Введите текст задачи");}
        model.addLast(text);
        input.clear();
        syncUi();
        status.setText("Добавлено: \"" + text + "\"");
    }

    private void move(int delta){
        int i = listView.getSelectionModel().getSelectedIndex();
        model.move(i, delta);
        syncUi();
        int target = Math.max(0, Math.min(i + delta, model.size() - 1));
        listView.getSelectionModel().select(target);
    }
//мост между логичкой и экраном
    private void syncUi() {
        uiItems.setAll(model.asList().stream().map(Task::toString).collect(Collectors.toList()));
        status.setText("Всего задач: " + model.size());
    }
}