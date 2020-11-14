package commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import date.Date;
import org.jetbrains.annotations.NotNull;
import enums.AbgabeOrt;
import enums.Subject;
import task.Task;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
        loadDataFromFile("src/main/resources/values.txt");
    }

    /**
     * Liest Daten aus einer Datei und speichert sie in die List.
     *
     * @param fileName File aus dem gelesen werden sollte.
     */
    private static void loadDataFromFile(@NotNull String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            ObjectMapper mapper = new ObjectMapper();
            while((line = reader.readLine()) != null){
                try {
                    allTasks.add(mapper.readValue(line, Task.class));
                } catch (JsonProcessingException e) {
                    System.err.println("JSON Konnte nicht geparst werden");
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Datei konnte nicht gefunden werden!");
        } catch (IOException e){
            System.err.println("Fehler beim Lesen der Datei!");
        }
    }

    /**
     * Gibt alle Tasks zurück welche bis zu diesem Datum zu erledigen sind.
     *
     * @param due Datum
     * @return Liste der Tasks die zu erledigen sind.
     */
    public static List<Task> getTasksDue(@NotNull Date due) {
        List<Task> tasksTillDueDate = new ArrayList<>();
        for (Task task : allTasks) {
            if (due.compareTo(task.getDueDate()) <= 0) {
                tasksTillDueDate.add(task);
            }
        }
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
        for (Task task : allTasks) {
            if (task.getSubject() == subject) {
                tasksInSubject.add(task);
            }
        }
        return tasksInSubject;
    }

    /***
     * Gibt einem alle Tasks für einen Abgabeort zurück.
     * @param ort Abgabeort
     * @return List mit allen Tasks von einem Abgabeort.
     */
    public static List<Task> getTasksInAbgabeOrt(@NotNull AbgabeOrt ort) {
        List<Task> tasksInOrt = new ArrayList<>();
        for (Task task : allTasks) {
            if (task.getAbgabeOrt() == ort) {
                tasksInOrt.add(task);
            }
        }
        return tasksInOrt;
    }

    /**
     * Gibt einem alle überfälligen Tasks zurück.
     *
     * @param date Datum
     * @return Alle überfälligen Tasks.
     */
    public static List<Task> getOverDueTasks(@NotNull Date date) {
        List<Task> tasksOverDue = new ArrayList<>();
        for (Task task : allTasks) {
            if (date.compareTo(task.getDueDate()) > 0) {
                tasksOverDue.add(task);
            }
        }
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
}