package subject;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.jetbrains.annotations.NotNull;

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
        if (this.subjectName.compareTo(o.subjectName) > 0)
            return this.subjectName.compareTo(o.subjectName);
        return this.subjectTeacher.compareTo(o.subjectTeacher);
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
}
