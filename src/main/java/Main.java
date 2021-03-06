
import date.DateTime;
import enums.AbgabeOrt;
import gui.TaskApp;
import handler.FileHandler;
import subject.Subject;
import task.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) throws FileNotFoundException {
//        System.out.println(Commands.getAllTasks().toString());
        writeTestData();
//        Preferences userNode = Preferences.userNodeForPackage(Main.class);
//
//        String myPreference = userNode.get("My Preference","adsf");
//        System.out.println(myPreference);

        System.out.println(FileHandler.readFileFromRessourceAsString("gui/setup/setup.fxml"));

//        String myPreference = userNode.get("My Preference","adsf");
//        System.out.println(myPreference);
//        System.out.println(System.getenv("APPDATA"));

    }

    private static void writeTestData() {
        List<Task> list = new ArrayList<>();
        list.add(new Task("adsf", "", new Subject("AM", "RIEE"), AbgabeOrt.TEAMS, new DateTime()));
        list.add(new Task("text", "", new Subject("POS", "SCRE"), AbgabeOrt.MOODLE, new DateTime()));
        list.add(new Task("cock", "", new Subject("TINF", "WEIX"), AbgabeOrt.EMAIL, new DateTime()));

        TaskApp asdf = new TaskApp();
        System.out.println("asdf" + "asdf");
//        try {
//            FileHandler.writeToFile(JSONHandler.listToJSONString(list, true), "src/main/resources/tasks.json");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
