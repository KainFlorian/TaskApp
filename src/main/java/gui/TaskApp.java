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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import subject.Subject;
import task.Task;

import java.io.File;
import java.io.IOException;
import java.util.Optional;


public class TaskApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        int row = 1; // wenn man etwas ändern möchte muss man nur die Reihenfolge ändern und nicht dauernd die indices ändern
        GridPane pane = new GridPane();
//        pane.setGridLinesVisible(true);
        pane.setVgap(10);
        pane.setHgap(20);
        pane.setPadding(new Insets(0, 20, 20, 20));

        // Menu
        MenuBar menu = new MenuBar();

        Menu settings = new Menu("Settings");

        MenuItem readSubjects = new MenuItem("select subject data");
        readSubjects.setOnAction(actionEvent -> {
            Optional<File> subjectFile = getFileFromChooser(stage, "Open Subject File");
            if(subjectFile.isPresent()){
                // TODO: value zuweisen
            }
        });

        MenuItem openTaskData = new MenuItem("Open Task Data");
        openTaskData.setOnAction(actionEvent -> {
            Optional<File> taskFile = getFileFromChooser(stage, "Open Task File");
            if(taskFile.isPresent()){
                // TODO: value zuweisen
            }
        });

        // TODO: add a save task directory
//        MenuItem setTaskSaveDirectory = new MenuItem("Set Task Save Directory");
//        setTaskSaveDirectory.setOnAction(actionEvent -> {
//
//        });

        settings.getItems().addAll(readSubjects, openTaskData);
        menu.getMenus().addAll(settings);
        pane.add(menu, 0,0, 4, 1);
        // -----------------------

        Label nameLabel = new Label("Titel:");
        TextField nameField = new TextField();
        nameField.setPromptText("Titel");
        pane.addRow(row++, nameLabel, nameField);

        ListView<Task> taskListView = new ListView<>();
        taskListView.getItems().addAll(Commands.getAllTasks());
        pane.add(taskListView,2,row-1,2,3);

        Label descriptionLabel = new Label("Beschreibung:");
        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Beschreibung...");
        descriptionField.setPrefHeight(200);
        pane.add(descriptionLabel, 0, row);
        pane.add(descriptionField, 1, row);
        row++;

        Label subjectLabel = new Label("Fach:");
        ComboBox<String> subjects = new ComboBox<>();
        subjects.getItems().addAll(Subject.fromFile(Files.LEHRER_3A.toString()));
        pane.add(subjectLabel, 0, row);
        pane.add(subjects, 1, row);
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

            if(dueDatePicker.getEditor().getText() == null){
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
                if(!dueDatePicker.getEditor().getText().matches("\\d{2}[.]\\d{2}[.]\\d{4}")){
                    throw new IllegalArgumentException("date");
                }
                System.out.println(dueDatePicker.getAccessibleText());
                DateTime time = DateTime.ofGUIString(dueDatePicker.getEditor().getText() + " " + dueTimeField.getText());
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
                dueDatePicker.getEditor().setText("");
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

        Button showDetails = new Button("Details");
        showDetails.setOnAction(actionEvent -> {
            Task selectedTask = taskListView.getSelectionModel().getSelectedItem();

            GridPane detailPane = new GridPane();
            detailPane.setVgap(10);
            detailPane.setHgap(20);
            detailPane.setPadding(new Insets(20));

            int rowIndex = 0;

            Label detailNameLabel = new Label("Titel: " + selectedTask.getName());
            detailPane.addRow(rowIndex++, detailNameLabel);

            Label detailDescriptionLabel = new Label("Beschreibung: ");
            Label detailDescription = new Label(selectedTask.getDescription());
            detailPane.addRow(rowIndex++, detailDescriptionLabel, detailDescription);

            Label detailSubject = new Label("Fach: " + selectedTask.getSubject());
            detailPane.addRow(rowIndex++, detailSubject);

            Label detailAbgabeOrt = new Label("Abgabe Ort: " + selectedTask.getAbgabeOrt());
            detailPane.addRow(rowIndex++, detailAbgabeOrt);

            Label detailDueDate = new Label(selectedTask.getDueDate().toString());
            detailPane.addRow(rowIndex, detailDueDate);

            Stage detailStage = new Stage();
            detailStage.setTitle("Details");
            detailStage.setScene(new Scene(detailPane));
            detailStage.show();
        });

        pane.addRow(row, addTask);
        pane.add(removeSelected, 2, 4);
        pane.add(showDetails, 3, 4);

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

    private static Optional<File> getFileFromChooser(@NotNull Stage stage,@NotNull String title){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        return selectedFile == null ? Optional.empty() : Optional.of(selectedFile);
    }
    
    private static void setError(Node set){
        set.setStyle("-fx-focus-color: red");
        set.requestFocus();
    }
}