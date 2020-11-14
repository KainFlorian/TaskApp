package enums;

import com.fasterxml.jackson.annotation.JsonGetter;

public enum Files {

    TASKS("src/main/resources/tasks.json");

    private String filepath;

    private Files(String filepath) {
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
