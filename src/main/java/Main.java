import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import commands.Commands;
import date.Date;
import date.DateTime;
import enums.AbgabeOrt;
import json.JSONHandler;
import subject.Subject;
import task.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        System.out.println(Commands.getAllTasks().toString());
        writeTestData();
    }

    private static void writeTestData() {
        List<Task> list = new ArrayList<>();
        list.add(new Task("adsf", "", new Subject("AM", "RIEE"), AbgabeOrt.TEAMS, new DateTime()));
        list.add(new Task("text", "", new Subject("POS", "SCRE"), AbgabeOrt.MOODLE, new DateTime()));
        list.add(new Task("cock", "", new Subject("TINF", "WEIX"), AbgabeOrt.EMAIL, new DateTime()));
        try {
            JSONHandler.writeToFile(JSONHandler.listToJSONString(list, true), "src/main/resources/tasks.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
