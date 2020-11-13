package task;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import subject.AbgabeOrt;
import subject.Subject;

import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private final String name;
    private final Subject subject;
    private final AbgabeOrt abgabeOrt;
    private final LocalDateTime dueDate;

    public Task(@NotNull @JsonProperty("name") String name, @NotNull @JsonProperty("subject") Subject subject,
                @NotNull @JsonProperty("abgabeOrt") AbgabeOrt abgabeOrt,@NotNull @JsonProperty("dueDate") LocalDateTime dueDate){
        this.name = name;
        this.subject = subject;
        this.abgabeOrt = abgabeOrt;
        this.dueDate = dueDate;
    }

    public String toJsonLine(){
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    @JsonGetter("name")
    public String getName() {
        return name;
    }

    @JsonGetter("subject")
    public Subject getSubject() {
        return subject;
    }

    @JsonGetter("abgabeOrt")
    public AbgabeOrt getAbgabeOrt() {
        return abgabeOrt;
    }

    @JsonGetter("dueDate")
    public LocalDateTime getDueDate() {
        return dueDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(name, task.name) &&
                subject == task.subject &&
                abgabeOrt == task.abgabeOrt &&
                Objects.equals(dueDate, task.dueDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, subject, abgabeOrt, dueDate);
    }
}
