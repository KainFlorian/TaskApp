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
import subject.Subject;
import task.Task;

import java.util.Arrays;

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
        ComboBox<String> abgabeOrte = new ComboBox<>();
        abgabeOrte.getItems().addAll(AbgabeOrt.toStringArray());
        pane.addRow(row++, abgabeOrtLabel, abgabeOrte);

        Label dueDateLabel = new Label("Abgabe Termin:");
        TextField dueDateField = new TextField();
        dueDateField.setPromptText("dd.mm.yyyy hh:mm");
        pane.addRow(row++, dueDateLabel, dueDateField);

        Button addTask = new Button("Hinzufügen");
        addTask.setOnAction(actionEvent -> {
//            Task task = new Task(nameField.getText(), descriptionField.getText(),
//                    abgabeOrte.getValue(), new DateTime(dueDateField.getText()));
        });
        pane.addRow(row, addTask);

        ListView<Task> taskListView = new ListView<>();
        pane.addColumn(2, taskListView);

        stage.setScene(new Scene(pane));
        stage.setTitle("TaskApp");
        stage.show();
    }
}
