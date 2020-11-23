package enums;

import com.fasterxml.jackson.annotation.JsonGetter;

public enum GUIFiles {

    SETUPFXML("gui/setup.fxml");

    private String filepath;

    private GUIFiles(String filepath) {
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
