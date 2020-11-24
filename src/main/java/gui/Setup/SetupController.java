package gui.Setup;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;


import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class SetupController implements Initializable {

    private SetupModel model;
    private Stage setupStage;

    @FXML
    private TextField pathInput;

    public Stage getSetupStage() {
        return setupStage;
    }

    public void setSetupStage(Stage setupStage) {
        this.setupStage = setupStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new SetupModel();
        //Standard install Path
        pathInput.setText(System.getProperty("user.home").toString() + "\\AppData\\Roaming\\TaskApp");

    }


    public void openInstallDirectory(){
        DirectoryChooser dc = new DirectoryChooser();
        File selectedDirectory = dc.showDialog(this.setupStage);

        pathInput.setText(selectedDirectory.toPath().toString() + "\\TaskApp");

    }
}
