package json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;


import java.io.*;

import java.util.List;

public class JSONHandler {

    /**
     * Wandelt eine Liste in einen JSON String um
     *
     * @param list Liste die in JSON geparst werden soll
     * @return JSON String von der Liste
     */
    @SuppressWarnings("rawtypes")//Greife nicht auf die Objecte in <code>list</code> zu, daher muss ich den generic nicht zuweisen
    public static String listToJSONString(@NotNull List list) throws IOException {
        return listToJSONString(list,true);
    }

    @SuppressWarnings("rawtypes")//Greife nicht auf die Objecte in <code>list</code> zu, daher muss ich den generic nicht zuweisen
    public static String listToJSONString(@NotNull List list, boolean prettifyOutput) throws IOException{
        final ObjectMapper mapper = new ObjectMapper();
        if(prettifyOutput){
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(list);
        }
        else{
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
    public static void writeToFile(@NotNull String text,@NotNull String file) throws IOException {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(text);
        }
    }

    /**
     * Fügt zu einem File den angegebenem Text hinzu.
     *
     * @param text der Text der geschrieben wird
     * @param file der Filename
     * @throws IOException Wird geworfen falls das File nicht gefunden wird
     */
    public static void appendToFile(@NotNull String text,@NotNull String file) throws IOException {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.append(text);
        }
    }

    /**
     * Generiert ein List Object mit der angegebenen JSON line
     *
     * @param line   JSON line die in eine Liste Umgewandelt werden soll
     * @param tClass Klasse des Listen Typs z.B.: Task.class
     * @param <T>    Generic für die Liste
     * @return Liste aus dem JSON string <code>line</code> erzeugten Objeckten <code>T</code>
     * @throws JsonProcessingException wird geworfen wenn <code>T</code> nicht Jackson erstellt werden kann
     */
    public static <T> List<T> listFromJSONSTRING(@NotNull String line,@NotNull Class<T> tClass) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(line, objectMapper.getTypeFactory().constructCollectionType(List.class, tClass));
    }
}
