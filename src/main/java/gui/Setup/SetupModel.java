package gui.Setup;

import handler.FileHandler;

import java.io.IOException;

public class SetupModel {


    public SetupModel(){

    }

    public void setInsallPath(String installPath){
        try {
            FileHandler.setInstallPath(installPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
