package subject;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public enum Subject {

    POS("Programmieren", "SCRE"),
    AM("Mathe", "RIEE"),
    TINF("Technische Informatik", "WEIX"),
    POS_DOE("Programmieren", "DOEL"),
    DBI("Datenbanken", "KRON"),
    NVS("Netzwerktechnik", "RAWO"),
    E("Englisch", "NEUL"),
    POS_REIO("Programmieren", "REIO"),
    GGP_DICH("Geografie", "DICH"),
    GGP_WISS("Geografie", "WISS"),
    D("Deutsch", "REIC"),
    SYP_CAST("SYP", "CAST"),
    SYP_SCBI("SYP", "SCBI"),
    BWM("BWM", "BUCH"),
    NW("NW", "HOEC");

    private String name;
    private String teacher;

    private Subject(String name, String teacher){
        this.name = name;
        this.teacher = teacher;
    }

    @JsonGetter("name")
    public String getName() {
        return name;
    }

    @JsonGetter("teacher")
    public String getTeacher() {
        return teacher;
    }

    @Override
    public String toString() {
        return "Subject: " + this.name + ", " + this.teacher;
    }
}
