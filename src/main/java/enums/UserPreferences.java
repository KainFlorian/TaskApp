package enums;

public enum UserPreferences {



    INSTALLPATH("installPath");


    private String pref;

    private UserPreferences(String pref){
        this.pref = pref;
    }

    @Override
    public String toString() {
        return this.pref;
    }
    public String getPref(){
        return this.pref;
    }
}
