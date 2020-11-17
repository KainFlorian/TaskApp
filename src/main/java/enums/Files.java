package enums;

import com.fasterxml.jackson.annotation.JsonGetter;

public enum Files {

    TASKS("src/main/resources/tasks.json"),
    LEHRER_3A("src/main/resources/3ALehrer.json");


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
