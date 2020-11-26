package gui.Setup;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;


import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class SetupController implements Initializable {


    private final SetupModel MODEL = new SetupModel();
    private Stage setupStage;

    @FXML
    private TextField pathInput;

    @FXML
    private Button abbrechenButton;
    @FXML
    public Button weiterButton;


    public void setSetupStage(Stage setupStage) {
        this.setupStage = setupStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Standard install Path
        pathInput.setText(System.getProperty("user.home") + "\\AppData\\Roaming\\TaskApp");
    }

    public void openInstallDirectory(){
        DirectoryChooser dc = new DirectoryChooser();
        File selectedDirectory = dc.showDialog(this.setupStage);
        if (selectedDirectory != null){
            pathInput.setText(selectedDirectory.toPath().toString() + "\\TaskApp");
        }
    }
    public void exit(){
        Platform.exit();
    }
    public void install(){
        MODEL.setInsallPath(pathInput.getText());
        Platform.exit();
    }
}
