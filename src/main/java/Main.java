import subject.Subject;
import task.Task;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        Task task = new Task("test", Subject.AM, "Teams", null);
        System.out.println(task.toJsonLine());
    }
}
