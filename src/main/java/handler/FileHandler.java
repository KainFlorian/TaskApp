package handler;

import enums.InitFiles;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URL;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.*;




public class FileHandler {

    private static String installPath = "";

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
        if(!getInstallPath().equals("")){
            Files.move(Paths.get(getInstallPath()),Paths.get(installPath),REPLACE_EXISTING);
        }
        else{
            init(installPath);
        }

        FileHandler.installPath = installPath;

    }

    /**
     * Wird verwendet um alle nötigen Files zu erstellen.
     *
     * @param installPath Der Pfad in dem alle Files erstellt werden sollen
     */
    public static void init(String installPath){
        for(InitFiles f : InitFiles.values()){
            String file = installPath + "/" + f.getFilepath();
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

    public static int reapair(){
        if(FileHandler.doAllFileExist()){
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
    public static boolean doAllFileExist(){
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


        try (InputStream is = FileHandler.class.getClassLoader().getResourceAsStream(fileName)) {
            if(is == null){
                throw new FileNotFoundException("File wurde nicht gefunden");
            }
            InputStreamReader isReader = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isReader);
            String str;

            while ((str = reader.readLine()) != null) {
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

        String file = installPath + "/" +fileName;
        try (OutputStream writer = new FileOutputStream(new File(file))) {
            writer.write(text.getBytes());
        }



    }
    public static void appendToFile(@NotNull String text, @NotNull String fileName) throws FileNotFoundException{
        String file = installPath + "/" +fileName;
        try (OutputStream writer = new FileOutputStream(new File(file),true)) {
            writer.write(text.getBytes());
        }
        catch( NullPointerException | IOException e){
            throw new FileNotFoundException("File wurde nicht gefunden");
        }
    }




}
