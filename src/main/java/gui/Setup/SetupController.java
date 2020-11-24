package gui.Setup;

import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class SetupController implements Initializable {

    private SetupModel model;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new SetupModel();
    }

    public void addCount(){
        model.setCount(model.getCount() + 1);

        System.out.println(model.getCount());
    }
}
