package handler;

import org.jetbrains.annotations.NotNull;

import java.io.*;


public class FileHandler {

    private static String installPath = "";

    public static String getInstallPath() {
        return installPath;
    }

    public static void setInstallPath(String installPath) {
        FileHandler.installPath = installPath;
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
    public static InputStream readFileFromRessourceInputStream(String fileName) throws FileNotFoundException{

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
    public static void writeToFile(@NotNull String text, @NotNull String fileName) throws FileNotFoundException {

        String file = installPath + "/" +fileName;
        try (OutputStream writer = new FileOutputStream(new File(file))) {
            writer.write(text.getBytes());
        }
        catch( NullPointerException | IOException e){
            throw new FileNotFoundException("File wurde nicht gefunden");
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
