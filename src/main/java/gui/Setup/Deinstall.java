package gui.Setup;

import handler.FileHandler;

public class Deinstall {
    //Die Main verwenden um alles wieder zu löschen, sowohl regex als auch files
    public static void main(String[] args) {
        FileHandler.deinstall();
    }
}
