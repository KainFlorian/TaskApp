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

    public static String[] toStringArray(){
        String[] ret = new String[AbgabeOrt.values().length];
        for(int i = 0; i < ret.length; i++){
            ret[i] = AbgabeOrt.values()[i].toString();
        }
        return ret;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
