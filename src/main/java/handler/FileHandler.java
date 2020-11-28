package handler;

import enums.InitFiles;
import enums.UserPreferences;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.prefs.Preferences;

import static java.nio.file.StandardCopyOption.*;

public class  FileHandler {

    private static final Preferences USER_PREFERENCE = Preferences.userNodeForPackage(FileHandler.class);

    private static String installPath = "";

    //initialisiert den installPath und hohlt es sich von der PrefrenceAPI
    static {
        System.out.println(USER_PREFERENCE.get(UserPreferences.INSTALLPATH.getPref(), "asdf"));
        try {
            FileHandler.setInstallPath(USER_PREFERENCE.get(UserPreferences.INSTALLPATH.getPref(),""));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getInstallPath() {
        return installPath;
    }

    /**
     * Setzt den neuen installPath und moved alle File vom alten zum neuen
     *
     * @param installPath Der neue Pfad
     * @throws IOException Wenn der alte Pfad nicht exestiert
     * @throws DirectoryNotEmptyException wenn der neue Ordner nicht leer ist
     * @throws SecurityException wenn wir nicht ausreichend Rechte haben den Ordner zu schreiben
     */
    public static void setInstallPath(String installPath) throws IOException {
        if(FileHandler.installPath.equals(installPath) || installPath.equals("")){
            return;
        }
        else if(!getInstallPath().equals("")){
            Files.move(Paths.get(getInstallPath()),Paths.get(installPath),REPLACE_EXISTING);
        }
        else{
            try{
                FileHandler.installPath = installPath;
                init(installPath);
            }
            catch (Exception e){
                e.printStackTrace();
                return;
            }
        }
        FileHandler.USER_PREFERENCE.put(UserPreferences.INSTALLPATH.getPref(), installPath);
    }

    /**
     * Wird verwendet um alle nötigen Files zu erstellen und den installPath in die user prefrences zu schreiben.
     *
     * @param installPath Der Pfad in dem alle Files erstellt werden sollen
     */
    public static void init(String installPath){
        for(InitFiles f : InitFiles.values()){
            String file = f.getFilepath();
            try{

                FileHandler.writeToFile("", file);
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * Findet Files die fehlen und erstellt sie neu
     *
     * @return Anzahl der neu erstellten files
     */
    public static int repair(){
        if(FileHandler.doAllFilesExist()){
            return 0;
        }
        int missingFiles = 0;

        for(InitFiles f : InitFiles.values()) {
            String file = installPath + "/" + f.getFilepath();
            File testFile = new File(file);
            if(testFile.exists() && !testFile.isDirectory()){
                missingFiles++;
                try{
                    FileHandler.writeToFile("",file);
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        return missingFiles;
    }

    /**
     * Checkt ob alle Files im Installationpfad noch exestieren
     * @return ob alle Files noch exestieren
     */
    public static boolean doAllFilesExist(){
        for(InitFiles f : InitFiles.values()) {
            String file = installPath + "/" + f.getFilepath();
            File testFile = new File(file);
            if (!(testFile.exists() && !testFile.isDirectory())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Liest ein File im installPath ein und liefert es als String zurück
     * @param fileName Name des Files was eingelesen wird
     * @return Inhalt des Files
     * @throws FileNotFoundException Wird geworfen wenn das File nicht gefunden werden kann. Das heißt die Files wurden extern bearbeitet und es muss reperiert werden.
     */
    public static String readFileAsString(String fileName) throws FileNotFoundException  {
        StringBuilder builder = new StringBuilder();
        String file = installPath + "/" + fileName;

        try (InputStream is = new FileInputStream(file)) {

            InputStreamReader isReader = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isReader);
            String str;

            while ((str = reader.readLine()) != null) {
                builder.append(str);
            }
        }
        catch(IOException e){
            throw new FileNotFoundException("File wurde nicht gefunden");
        }
        return builder.toString();
    }

    /**
     * Liest ein File im installPath ein und liefert es als InputStream zurück
     * @param fileName Name des Files was eingelesen wird
     * @return InputStream mit dem file inhalt.
     * @throws FileNotFoundException Wird geworfen wenn das File nicht gefunden werden kann. Das heißt die Files wurden extern bearbeitet und es muss reperiert werden.
     */
    public static InputStream readFileAsInputStream(String fileName) throws FileNotFoundException{
        String file = installPath + "/" + fileName;
        return new FileInputStream(file);
    }

    /**
     * Liest ein File von Ressource ein und liefert es als String zurück
     * @param fileName Name des Files was eingelesen wird
     * @return Inhalt des Files
     * @throws FileNotFoundException Tritt auf wenn das File in ressource nicht exestiert
     */
    public static String readFileFromRessourceAsString(String fileName) throws FileNotFoundException{
        StringBuilder builder = new StringBuilder();
        String str;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(FileHandler.readFileAsInputStream(fileName), "UTF-8"));) {
            while((str = br.readLine()) != null) {
                builder.append(str);
            }
        }
        catch(IOException e){
            throw new FileNotFoundException("File in ressource not found");
        }
        return builder.toString();
    }

    /**
     * Liest ein File von Ressource ein und liefert es als InputStream zurück
     * @param fileName Name des Files was eingelesen wird
     * @return Inhalt des Files als InputStream
     * @throws FileNotFoundException Tritt auf wenn das File in ressource nicht exestiert
     */
    public static InputStream readFileFromRessourceAsInputStream(String fileName) throws FileNotFoundException{
        InputStream is;
        is = FileHandler.class.getClassLoader().getResourceAsStream(fileName);
        if(is == null){
            throw new FileNotFoundException("File in resource not found");
        }
        return is;
    }

    /**
     * Erstellt ein File mit dem angegebenem Text.
     * Gibt es das File bereits wird es gelöscht.
     *
     * @param text der Text der geschrieben wird
     * @param fileName der Filename
     * @throws FileNotFoundException Wird geworfen falls das File nicht gefunden wird
     */
    public static void writeToFile(@NotNull String text, @NotNull String fileName)  throws IOException{

        String path = installPath + "\\" +fileName;

        File file = new File(path);
        if (!file.exists()){
            file.getParentFile().mkdir();
            file.createNewFile();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(text);
        }
    }

    public static void appendToFile(@NotNull String text, @NotNull String fileName) throws FileNotFoundException{
        String file = installPath + "\\" +fileName;
        try (OutputStream writer = new FileOutputStream(new File(file),true)) {
            writer.write(text.getBytes());
        }
        catch( NullPointerException | IOException e){
            throw new FileNotFoundException("File wurde nicht gefunden");
        }
    }

    /**
     *
     */
    public static void deinstall(){
        USER_PREFERENCE.remove(UserPreferences.INSTALLPATH.getPref());
        deleteDirectory(new File(installPath));

    }
    private static boolean  deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }
}