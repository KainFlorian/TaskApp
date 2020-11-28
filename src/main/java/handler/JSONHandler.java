package handler;

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
    @SuppressWarnings("rawtypes")
//Greife nicht auf die Objecte in <code>list</code> zu, daher muss ich den generic nicht zuweisen
    public static String listToJSONString(@NotNull List list) throws IOException {
        return listToJSONString(list, true);
    }

    @SuppressWarnings("rawtypes")
//Greife nicht auf die Objecte in <code>list</code> zu, daher muss ich den generic nicht zuweisen
    public static String listToJSONString(@NotNull List list, boolean prettifyOutput) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        return prettifyOutput ? mapper.writerWithDefaultPrettyPrinter().writeValueAsString(list)
                              : mapper.writeValueAsString(list);
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
        return listFromJSONSTRING(FileHandler.readFileFromRessourceAsString(fileName),tClass);
    }
}
