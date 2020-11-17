package enums;

import java.util.Arrays;

public enum AbgabeOrt {

    TEAMS("Teams"),
    MOODLE("Moodle"),
    EMAIL("E-Mail");

    private String name;

    private AbgabeOrt(String ort) {
        this.name = ort;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
