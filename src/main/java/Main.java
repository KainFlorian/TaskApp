import commands.Commands;
import date.DateTime;
import enums.AbgabeOrt;
import enums.Files;
import json.JSONHandler;
import subject.Subject;
import task.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        System.out.println(Commands.getAllTasks().toString());

//        writeTestData();
    }

    private static void writeTestData() {
        List<Task> list = new ArrayList<>();
        list.add(new Task("adsf", "hü nr...", new Subject("AM", "RIEE"), AbgabeOrt.TEAMS, new DateTime()));
        list.add(new Task("text","decode", new Subject("POS", "SCRE"), AbgabeOrt.MOODLE, new DateTime()));
        list.add(new Task("cock", "skejdular",new Subject("TINF", "WEIX"), AbgabeOrt.EMAIL, new DateTime()));
        try {
            JSONHandler.writeToFile(JSONHandler.listToJSONString(list, true), "src/main/resources/tasks.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void write3ATeacher(){
        List<Subject> list = new ArrayList<>();
        list.add(new Subject("POS", "SCRE"));
        list.add(new Subject("AM", "RIEE"));
        list.add(new Subject("D", "REIC"));
        list.add(new Subject("E", "NEUL"));
        list.add(new Subject("POS_DOEL", "DOEL"));
        list.add(new Subject("POS_REIO", "REIO"));
        list.add(new Subject("TINF", "WEIX"));
        list.add(new Subject("SYP_CAST", "CAST"));
        list.add(new Subject("SYP_SCBI", "SCBI"));
        list.add(new Subject("NVS", "RAAB"));
        list.add(new Subject("DBI", "KRON"));
        list.add(new Subject("GGP_DICH", "DICH"));
        list.add(new Subject("GGP_WISS", "WISS"));
        list.add(new Subject("BWM", "BUCH"));
        list.add(new Subject("RK", "KRED"));
        list.add(new Subject("RK", "KRED"));
        list.add(new Subject("BESP", "NEUl"));
        try {
            JSONHandler.writeToFile(JSONHandler.listToJSONString(list, true), Files.LEHRER_3A.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
