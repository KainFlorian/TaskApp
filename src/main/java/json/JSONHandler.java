package json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import task.Task;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class JSONHandler {

    /**
     * Wandelt eine Liste in einen JSON String um
     *
     * @param list Liste die in JSON geparst werden soll
     * @return JSON String von der Liste
     *
     */
    @SuppressWarnings("rawtypes")//Greife nicht auf die Objecte in <code>list</code> zu, daher muss ich den generic nicht zuweisen
    public static String listToJSONString(List list) throws IOException{

        if(list == null){
            throw new NullPointerException();
        }

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectMapper mapper = new ObjectMapper();

        mapper.writeValue(out,list);

        final byte[] data = out.toByteArray();

        return new String(data);
    }

    /**
     * Erstellt ein File mit dem angegebenem Text.
     * Gibt es das File bereits wird es gelöscht.
     *
     * @param text der Text der geschrieben wird
     * @param file der Filename
     * @throws IOException Wird geworfen falls das File nicht gefunden wird
     */
    public static void writeToFile(String text, String file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(text);

        writer.close();
    }

    /**
     * Fügt zu einem File den angegebenem Text hinzu.
     *
     *
     * @param text der Text der geschrieben wird
     * @param file der Filename
     * @throws IOException Wird geworfen falls das File nicht gefunden wird
     */
    public static void appendToFILE(String text, String file) throws IOException{
        BufferedWriter writer= new BufferedWriter(new FileWriter(file));
        writer.append(text);

        writer.close();
    }

    /**
     * Generiert ein List Object mit der angegebenen JSON line
     * @param line JSON line die in eine Liste Umgewandelt werden soll
     * @param tClass Klasse des Listen Typs z.B.: Task.class
     * @param <T> Generic für die Liste
     * @return Liste aus dem JSON string <code>line</code> erzeugten Objeckten <code>T</code>
     * @throws JsonProcessingException wird geworfen wenn <code>T</code> nicht Jackson erstellt werden kann
     */
    public static <T> List<T> listFromJSONSTRING(String line, Class<T> tClass) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(line, objectMapper.getTypeFactory().constructCollectionType(List.class, tClass));
    }


}
