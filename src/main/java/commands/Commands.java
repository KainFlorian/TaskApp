package commands;

import date.DateTime;
import enums.InitFiles;
import handler.FileHandler;
import javafx.collections.ObservableList;
import handler.JSONHandler;

import org.jetbrains.annotations.NotNull;
import enums.AbgabeOrt;
import subject.Subject;
import task.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/*
    TODO: loadDataFromFile Funktion, warte bis Tasks gespeichert werden können.
 */

public class Commands {

    private Commands() {
    }

    private static List<Task> allTasks;

    static {
        allTasks = new ArrayList<>();
        loadDataFromFile(InitFiles.TASKS.toString());
    }

    /**
     * Liest Daten aus einer Datei und speichert sie in die List.
     *
     * @param fileName File aus dem gelesen werden sollte.
     */
    private static void loadDataFromFile(@NotNull String fileName) {
        try {
            allTasks = JSONHandler.listFromFile(fileName,Task.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Task> getAllTasks(){
        return allTasks;
    }

    /**
     * Gibt alle Tasks zurück welche bis zu diesem Datum zu erledigen sind.
     *
     * @param due Datum
     * @return Liste der Tasks die zu erledigen sind.
     */
    public static List<Task> getTasksDue(@NotNull DateTime due) {
        List<Task> tasksTillDueDate = new ArrayList<>();
        allTasks.forEach(task -> {
            if (task.getDueDate().compareTo(due) <= 0) {
                tasksTillDueDate.add(task);
            }
        });
        return tasksTillDueDate;
    }

    /**
     * Gibt einem eine List der Tasks in einem Fach zurück.
     *
     * @param subject Fach
     * @return List mit allen Aufgaben in einem bestimmten Fach.
     */
    public static List<Task> getTasksInSubject(@NotNull Subject subject) {
        List<Task> tasksInSubject = new ArrayList<>();
        allTasks.forEach(task -> {
            if (task.getSubject().equals(subject)) {
                tasksInSubject.add(task);
            }
        });
        return tasksInSubject;
    }

    /***
     * Gibt einem alle Tasks für einen Abgabeort zurück.
     * @param ort Abgabeort
     * @return List mit allen Tasks von einem Abgabeort.
     */
    public static List<Task> getTasksInAbgabeOrt(@NotNull AbgabeOrt ort) {
        List<Task> tasksInOrt = new ArrayList<>();
        allTasks.forEach(task -> {
            if (task.getAbgabeOrt() == ort) {
                tasksInOrt.add(task);
            }
        });
        return tasksInOrt;
    }

    /**
     * Gibt einem alle überfälligen Tasks zurück.
     *
     * @param date Datum
     * @return Alle überfälligen Tasks.
     */
    public static List<Task> getOverDueTasks(@NotNull DateTime date) {
        List<Task> tasksOverDue = new ArrayList<>();
        allTasks.forEach(task -> {
            if(date.compareTo(task.getDueDate()) > 0){
                tasksOverDue.add(task);
            }
        });
        return tasksOverDue;
    }

    /**
     * Gibt eine List zurück, die sortiert ist nach DueDate.
     *
     * @return Sortierte Liste nach DueDate.
     */
    public static List<Task> getTasksSortedByDueDate() {
        Comparator<Task> byDate = Comparator.comparing(Task::getDueDate);
        List<Task> sorted = new ArrayList<>(allTasks);
        sorted.sort(byDate);
        return sorted;
    }

    /**
     * Gibt eine List zurück, die sortiert ist nach Subject.
     *
     * @return Sortierte Liste nach Subject.
     */
    public static List<Task> getTasksSortedBySubject() {
        Comparator<Task> bySubject = Comparator.comparing(Task::getSubject);
        List<Task> sorted = new ArrayList<>(allTasks);
        sorted.sort(bySubject);
        return sorted;
    }

    /**
     * Gibt eine List zurück, welche nach Namen sortiert ist.
     *
     * @return Sortierte List nach Name.
     */
    public static List<Task> getTasksSortedByName() {
        Comparator<Task> byName = Comparator.comparing(Task::getName);
        List<Task> sorted = new ArrayList<>(allTasks);
        sorted.sort(byName);
        return sorted;
    }

    /**
     * Löscht einen Task
     *
     * @param task Task der gelöscht werden soll.
     */
    public static void removeTask(@NotNull Task task) {
        allTasks.remove(task);
    }

    public static void removeTask(int index){
        allTasks.remove(index);
    }

    public static void removeTask(@NotNull ObservableList<Integer> tasks){
        for(int i : tasks){
            removeTask(i);
        }
    }

    /**
     * Fügt einen Task zur Liste <code>alltasks</code> hinzu.
     * @param task Task welcher hinzugefügt werden soll.
     */
    public static void addTask(@NotNull Task task){
        allTasks.add(task);
    }

    public static void saveData(@NotNull String fileName) throws IOException {
        FileHandler.writeToFile(JSONHandler.listToJSONString(allTasks, true), fileName);
    }
}