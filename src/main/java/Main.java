import commands.Commands;
import date.Date;
import enums.AbgabeOrt;
import enums.Subject;
import json.JSONHandler;
import task.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println(Commands.getTasksSortedByName());
    }
}
