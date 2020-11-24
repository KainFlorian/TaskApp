package gui.Setup;

import enums.GUIFiles;
import handler.FileHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;


public class Setup extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();

        Parent root = fxmlLoader.load(FileHandler.readFileFromRessourceAsInputStream(GUIFiles.SETUPFXML.getFilepath()));

        primaryStage.setTitle("Setup");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }


}
