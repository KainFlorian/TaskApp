package enums;

import com.fasterxml.jackson.annotation.JsonGetter;

public enum InitFiles {

    TASKS("tasks.json"),
    LEHRER("Lehrer.json");


    private String filepath;

    private InitFiles(String filepath) {
        this.filepath = filepath;
    }

    @JsonGetter("filepath")
    public String getFilepath() {
        return filepath;
    }

    @Override
    public String toString() {
        return filepath;
    }
}
