import date.Date;
import enums.AbgabeOrt;
import enums.Files;
import enums.Subject;
import json.JSONHandler;
import task.Task;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Task> a = new ArrayList<>();
        a.add(new Task("asdf",Subject.AM, AbgabeOrt.EMAIL,new Date()));
        a.add(new Task("bsdf",Subject.D, AbgabeOrt.EMAIL,new Date()));
        a.add(new Task("csdf",Subject.E, AbgabeOrt.EMAIL,new Date()));
        System.out.println("jojo");
        try {
            JSONHandler.writeToFile(JSONHandler.listToJSONString(a), Files.TASKS.getFilepath());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
