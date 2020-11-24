package gui.Setup;

import enums.GUIFiles;
import handler.FileHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;




public class Setup extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();

        Parent root = fxmlLoader.load(FileHandler.readFileFromRessourceAsInputStream(GUIFiles.SETUPFXML.getFilepath()));
        ((SetupController) fxmlLoader.getController()).setSetupStage(primaryStage);
        primaryStage.setTitle("Setup");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }


}
