package gui;

import date.DateTime;
import enums.AbgabeOrt;
import enums.Files;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateTimeStringConverter;
import subject.Subject;
import task.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.FormatStyle;
import java.util.Arrays;

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

        Label descriptionLabel = new Label("Beschreibung:");
        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Beschreibung...");
        descriptionField.setPrefHeight(200);
        pane.addRow(row++, descriptionLabel, descriptionField);

        Label subjectLabel = new Label("Fach:");
        ComboBox<String> subjects = new ComboBox<>();
        subjects.getItems().addAll(Subject.fromFile(Files.LEHRER_3A.toString()));
        pane.addRow(row++, subjectLabel, subjects);

        Label abgabeOrtLabel = new Label("Abgabe Ort:");
        ComboBox<AbgabeOrt> abgabeOrte = new ComboBox<>();
        abgabeOrte.getItems().addAll(AbgabeOrt.values());
        pane.addRow(row++, abgabeOrtLabel, abgabeOrte);

        Label dueDateLabel = new Label("Abgabe Termin:");
        DatePicker dueDatePicker = new DatePicker();
        pane.addRow(row++, dueDateLabel, dueDatePicker);

        Button addTask = new Button("Hinzufügen");
        addTask.setOnAction(actionEvent -> {
            String[] splittedSubject = subjects.getValue().split(":");
            if(splittedSubject.length != 2){
                subjects.setStyle("-fx-focus-color: red");
                subjects.requestFocus();
                return;
            }
            LocalDate time = dueDatePicker.getValue();
            System.out.println(time);
            DateTime dateTime = new DateTime(time.getDayOfMonth(), time.getMonthValue(), time.getYear());
            Task task = new Task(nameField.getText(), descriptionField.getText(),
                    new Subject(splittedSubject[0], splittedSubject[1]),
                    abgabeOrte.getValue(), dateTime);
            System.out.println(task);
        });
        pane.addRow(row, addTask);

        ListView<Task> taskListView = new ListView<>();
        pane.addColumn(2, taskListView);


        stage.setScene(new Scene(pane));
        stage.setTitle("TaskApp");
        stage.show();
    }
}
