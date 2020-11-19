package json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import subject.Subject;
import task.Task;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class JSONHandler {

    /**
     * Wandelt eine Liste in einen JSON String um
     *
     * @param list Liste die in JSON geparst werden soll
     * @return JSON String von der Liste
     */
    @SuppressWarnings("rawtypes")
//Greife nicht auf die Objecte in <code>list</code> zu, daher muss ich den generic nicht zuweisen
    public static String listToJSONString(@NotNull List list) throws IOException {
        return listToJSONString(list, true);
    }

    @SuppressWarnings("rawtypes")
//Greife nicht auf die Objecte in <code>list</code> zu, daher muss ich den generic nicht zuweisen
    public static String listToJSONString(@NotNull List list, boolean prettifyOutput) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        if (prettifyOutput) {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(list);
        } else {
            return mapper.writeValueAsString(list);
        }
    }

    /**
     * Erstellt ein File mit dem angegebenem Text.
     * Gibt es das File bereits wird es gelöscht.
     *
     * @param text der Text der geschrieben wird
     * @param file der Filename
     * @throws IOException Wird geworfen falls das File nicht gefunden wird
     */
    public static void writeToFile(@NotNull String text, @NotNull String file) throws IOException {
        ClassLoader classLoader = JSONHandler.class.getClassLoader();
        try (OutputStream writer = new FileOutputStream(new File(classLoader.getResource(file).getFile()))) {
            writer.write(text.getBytes());
        }


    }

    /**
     * Fügt zu einem File den angegebenem Text hinzu.
     *
     * @param text der Text der geschrieben wird
     * @param file der Filename
     * @throws IOException Wird geworfen falls das File nicht gefunden wird
     */
    public static void appendToFile(@NotNull String text, @NotNull String file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.append(text);
        }
    }

    /**
     * Generiert ein List Object mit der angegebenen JSON line
     *
     * @param line   JSON line die in eine Liste Umgewandelt werden soll
     * @param tClass Klasse von dem dem Generic muss übergeben werden da man die Klasse nicht in der Funktion ermittlen kann.
     * @param <T>    Generic für die Liste
     * @return Liste aus dem JSON string <code>line</code> erzeugten Objeckten <code>T</code>
     * @throws JsonProcessingException wird geworfen wenn <code>T</code> nicht mit Jackson erstellt werden kann
     */
    public static <T> List<T> listFromJSONSTRING(@NotNull String line,Class<T> tClass) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(line,objectMapper.getTypeFactory().constructCollectionType(List.class, tClass));
    }

    /**
     *
     * @param fileName Filename des umzuwandelnen Files
     * @param tClass Klasse von dem dem Generic muss übergeben werden da man die Klasse nicht in der Funktion ermittlen kann.
     * @param <T> Generic für die Liste
     * @return Liste aus dem File erzeugten Objeckten <code>T</code>
     * @throws IOException Falls das  File nicht Gefunden wird oder das Json nicht processed werden kann
     */
    public static <T> List<T> listFromFile(@NotNull String fileName,Class<T> tClass) throws IOException {
        StringBuilder builder = new StringBuilder();

        try(InputStream is = JSONHandler.class.getClassLoader().getResourceAsStream(fileName)){
            InputStreamReader isReader = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isReader);
            String str;
            while((str = reader.readLine())!= null){
                builder.append(str);
            }
        }
        return listFromJSONSTRING(builder.toString(),tClass);
    }


}
