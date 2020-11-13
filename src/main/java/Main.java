import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import commands.Commands;
import date.Date;
import subject.AbgabeOrt;
import subject.Subject;
import task.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        System.out.println(Commands.getTasksSortedByName());
    }

}
