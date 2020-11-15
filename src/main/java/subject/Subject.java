package subject;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


@SuppressWarnings("rawtypes")//Fuck Comparable
public class Subject implements Comparable {
     private String subjectName;
     private String subjectTeacher;

     //TODO Vielleicht noch Uhrzeit speichern

    public Subject(@NotNull String subjectName, @NotNull String subjectLehrer) {
        this.subjectName = subjectName;
        this.subjectTeacher = subjectLehrer;
    }

    @JsonGetter("subjectName")
    public String getSubjectName() {
        return subjectName;
    }

    @JsonSetter("subjectName")
    public void setSubjectName(@NotNull String subjectName) {
        this.subjectName = subjectName;
    }

    @JsonGetter("subjectTeacher")
    public String getSubjectTeacher() {
        return subjectTeacher;
    }

    @JsonSetter("subjectTeacher")
    public void setSubjectTeacher(@NotNull String subjectLehrer) {
        this.subjectTeacher = subjectLehrer;
    }


    @Override
    public int compareTo(@NotNull Object o) {
        if(o instanceof Subject){
            if(this.subjectName.compareTo(((Subject) o).subjectName) > 0){
                return this.subjectName.compareTo(((Subject) o).subjectName);
            }
            else return Math.max(this.subjectTeacher.compareTo(((Subject) o).subjectTeacher), 0);
        }
        return 1;
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

}
