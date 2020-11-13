package task;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import subject.Subject;

import java.time.LocalDateTime;

public class Task {
    private String name;
    private Subject subject;
    private String abgabeOrt;
    private LocalDateTime dueDate;

    public Task(@NotNull @JsonProperty("name") String name, @NotNull @JsonProperty("subject") Subject subject,
                @NotNull @JsonProperty("abgabeOrt") String abgabeOrt,@JsonProperty LocalDateTime dueDate){
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
    public String getAbgabeOrt() {
        return abgabeOrt;
    }

    @JsonGetter("dueDate")
    public LocalDateTime getDueDate() {
        return dueDate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Task{");
        sb.append("name='").append(name).append('\'');
        sb.append(", subject=").append(subject);
        sb.append(", abgabeOrt='").append(abgabeOrt).append('\'');
        sb.append(", dueDate=").append(dueDate);
        sb.append('}');
        return sb.toString();
    }
}
