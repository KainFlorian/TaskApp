package gui;

import commands.Commands;
import date.DateTime;
import enums.AbgabeOrt;
import enums.Files;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateTimeStringConverter;
import subject.Subject;
import task.Task;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.stream.Stream;

import static java.time.format.FormatStyle.MEDIUM;

public class TaskApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        int row = 0; // wenn man etwas ändern möchte muss man nur die Reihenfolge ändern und nicht dauernd die indices ändern
        GridPane pane = new GridPane();
        pane.setVgap(10);
        pane.setHgap(20);
        pane.setPadding(new Insets(20));

        Label nameLabel = new Label("Titel:");
        TextField nameField = new TextField();
        nameField.setPromptText("Titel");
        pane.addRow(row++, nameLabel, nameField);

        ListView<Task> taskListView = new ListView<>();
        taskListView.getItems().addAll(Commands.getAllTasks());
        pane.add(taskListView,2,0,1,3);

        Label descriptionLabel = new Label("Beschreibung:");
        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Beschreibung...");
        descriptionField.setPrefHeight(200);
        pane.add(descriptionLabel, 0, 1);
        pane.add(descriptionField, 1, 1);
        row++;

        Label subjectLabel = new Label("Fach:");
        ComboBox<String> subjects = new ComboBox<>();
        subjects.getItems().addAll(Subject.fromFile(Files.LEHRER_3A.toString()));
        pane.add(subjectLabel, 0, 2);
        pane.add(subjects, 1, 2);
        row++;

        Label abgabeOrtLabel = new Label("Abgabe Ort:");
        ComboBox<AbgabeOrt> abgabeOrte = new ComboBox<>();
        abgabeOrte.getItems().addAll(AbgabeOrt.values());
        pane.addRow(row++, abgabeOrtLabel, abgabeOrte);

        Label dueDateLabel = new Label("Abgabe Termin:");
        DatePicker dueDatePicker = new DatePicker();
        TextField dueTimeField = new TextField();
        dueTimeField.setPromptText("Uhrzeit");
        pane.addRow(row++, dueDateLabel, dueDatePicker, dueTimeField);

        Button addTask = new Button("Hinzufügen");
        addTask.setOnAction(actionEvent -> {
            pane.getChildren() // alle Nodes wieder normal formatieren
                    .forEach(node -> node.setStyle("-fx-focus-color: #0093ff"));

            if(dueDatePicker.getValue().toString() == null){
                setError(dueDatePicker);
                return;
            }
            if(dueTimeField.getText() == null || dueTimeField.getText().isEmpty()){
                setError(dueTimeField);
                return;
            }
            if(nameField.getText() == null || nameField.getText().isEmpty()){
                setError(nameField);
                return;
            }
            if(subjects.getValue() == null || subjects.getValue().isEmpty()){
                setError(subjects);
                return;
            }
            if(abgabeOrte.getValue() == null || subjects.getValue().isEmpty()){
                setError(abgabeOrte);
                return;
            }

            String[] splittedSubject = subjects.getValue().split(":");
            if(splittedSubject.length != 2){
                subjects.setStyle("-fx-focus-color: red");
                subjects.requestFocus();
                return;
            }
            try {
                if(!dueDatePicker.getValue().toString().matches("\\d{4}[-]\\d{2}[-]\\d{2}")){
                    throw new IllegalArgumentException("date");
                }
                DateTime time = DateTime.ofGUIString(dueDatePicker.getValue().toString() + " " + dueTimeField.getText());
                Task task = new Task(nameField.getText(),
                        descriptionField.getText() == null ? "" : descriptionField.getText(),
                        new Subject(splittedSubject[0], splittedSubject[1]),
                        abgabeOrte.getValue(), time);

                Commands.addTask(task);
                taskListView.getItems().clear();
                taskListView.getItems().addAll(Commands.getAllTasks());

                nameField.setText("");
                descriptionField.setText("");
                subjects.setValue("");
                dueTimeField.setText("");
            } catch (IllegalArgumentException e){
                if(e.getMessage().contains("date")){
                    setError(dueDatePicker);
                } else if(e.getMessage().contains("time")){
                    setError(dueTimeField);
                } else {
                    setError(dueDatePicker);
                    setError(dueTimeField);
                }
            }
        });

        Button removeSelected = new Button("Finished");
        removeSelected.setOnAction(actionEvent -> {
            Commands.removeTask(taskListView.getSelectionModel().getSelectedIndices());
            taskListView.getItems().clear();
            taskListView.getItems().addAll(Commands.getAllTasks());
        });

        pane.addRow(row, addTask, removeSelected);

        stage.setScene(new Scene(pane));
        stage.setTitle("TaskApp");
        stage.show();
    }
    
    @Override
    public void stop(){
        try {
            Commands.saveData(Files.TASKS.toString());
        } catch (IOException e) {
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("ERROR");
            err.setContentText("Fehler beim Speichern der Daten");
        }
    }
    
    private static void setError(Node set){
        set.setStyle("-fx-focus-color: red");
        set.requestFocus();
    }
}
