package subject;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import handler.JSONHandler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


public class Subject implements Comparable<Subject> {
    private String subjectName;
    private String subjectTeacher;

    //TODO Vielleicht noch Uhrzeit speichern

    public Subject(@NotNull @JsonProperty("subjectName") String subjectName, @NotNull @JsonProperty("subjectTeacher") String subjectLehrer) {
        this.subjectName = subjectName;
        this.subjectTeacher = subjectLehrer;
    }

    @JsonGetter("subjectName")
    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(@NotNull String subjectName) {
        this.subjectName = subjectName;
    }

    @JsonGetter("subjectTeacher")
    public String getSubjectTeacher() {
        return subjectTeacher;
    }

    public void setSubjectTeacher(@NotNull String subjectLehrer) {
        this.subjectTeacher = subjectLehrer;
    }

    @Override
    public int compareTo(@NotNull Subject o) {
        return Comparator.comparing(Subject::getSubjectName)
                .thenComparing(Subject::getSubjectTeacher)
                .compare(this, o);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return Objects.equals(subjectName, subject.subjectName) &&
                Objects.equals(subjectTeacher, subject.subjectTeacher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectName, subjectTeacher);
    }

    @Override
    public String toString() {
        return this.subjectName + ": " + this.subjectTeacher;
    }

    public static List<String> fromFile(String fileName) throws IOException{
        List<Subject> fromFile = JSONHandler.listFromFile(fileName,Subject.class);
        List<String> subjects = new ArrayList<>();
        fromFile.stream()
                .map(Subject::toString)
                .sorted(String::compareTo)
                .forEach(subjects::add);
        return subjects;
    }
}
