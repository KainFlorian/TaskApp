package task;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import date.Date;
import org.jetbrains.annotations.NotNull;
import enums.AbgabeOrt;
import subject.Subject;

import java.util.Objects;

public class Task {
    private final String name;
    private final Subject subject;
    private final AbgabeOrt abgabeOrt;
    private final Date dueDate;

    @JsonIgnore
    public final int SUBJECT_FIRST = 1;
    @JsonIgnore
    public final int DATE_FIRST = 2;
    @JsonIgnore
    public final int ORT_FIRST = 3;
    @JsonIgnore
    public final int NAME_FIRST = 3;


    public Task(@NotNull @JsonProperty("name") String name, @NotNull @JsonProperty("subject") Subject subject,
                @NotNull @JsonProperty("abgabeOrt") AbgabeOrt abgabeOrt, @NotNull @JsonProperty("dueDate") Date dueDate) {
        this.name = name;
        this.subject = subject;
        this.abgabeOrt = abgabeOrt;
        this.dueDate = dueDate;
    }

    @Deprecated
    public String toJsonLine() {
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
    public Date getDueDate() {
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

    public String toString(int format) {
        switch (format) {
            case 1:
                return this.subject + "\n\t" + this.name + "\n\t" + this.abgabeOrt + "\n\t" + this.dueDate + "\n";
            case 2:
                return this.dueDate + "\n\t" + this.name + "\n\t" + this.subject + "\n\t" + this.abgabeOrt + "\n";
            case 3:
                return this.abgabeOrt + "\n\t" + this.name + "\n\t" + this.subject + "\n\t" + this.dueDate + "\n";
            case 4:
                return this.name + "\n\t" + this.subject + "\n\t" + this.abgabeOrt + "\n\t" + this.dueDate + "\n";
        }
        return toString();
    }

    @Override
    public String toString() {
        return this.toString(NAME_FIRST);
    }
}
