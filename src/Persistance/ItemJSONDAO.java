package Persistance;

import Business.Item.Item;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Clase para manejar la lectura, el análisis y el guardado de ítems desde y hacia archivos JSON.
 */
public class ItemJSONDAO {

    /**
     * Lee y analiza un archivo JSON para crear una lista de objetos Item.
     *
     * @param filePath Ruta del archivo JSON donde se encuentran los datos de los ítems.
     * @return Una lista de objetos Item que representan los ítems leídos desde el archivo.
     */
    public static List<Item> readAndParseItems(String filePath) {
        List<Item> items = new ArrayList<>();

        try {
            // Leemos todo el contenido del archivo
            String allFileContent = new String(Files.readAllBytes(Paths.get(filePath)));
            // Convertimos el contenido en un JSONArray
            JSONArray jsonArray = new JSONArray(allFileContent);

            // Iteramos sobre el JSONArray y creamos los objetos Item
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // Extraemos los valores directamente, asumiendo que las claves existen
                int id = jsonObject.getInt("id");
                String name = jsonObject.getString("name");
                int power = jsonObject.getInt("power");
                int durability = jsonObject.getInt("durability");
                String itemClass = jsonObject.getString("class");

                // Agregamos el ítem a la lista
                items.add(new Item(id, name, itemClass, power, durability));
            }
        } catch (JSONException | IOException e) {
            System.err.println("Error: could not parse file! " + e.getMessage());
        }

        return items;
    }

    /**
     * Guarda la lista de ítems en un archivo JSON.
     *
     * @param items Lista de objetos Item que se van a guardar.
     * @param filePath Ruta del archivo donde se guardarán los ítems.
     * @throws IOException Si ocurre un error al escribir el archivo.
     * @throws JSONException Si ocurre un error al crear el formato JSON.
     */
    public static void saveItems(List<Item> items, String filePath) throws IOException, JSONException {
        JSONArray jsonArray = new JSONArray();

        // Creamos un JSONArray a partir de la lista de ítems
        for (Item item : items) {
            JSONObject itemObject = new JSONObject();
            itemObject.put("id", item.getId());
            itemObject.put("name", item.getName());
            itemObject.put("class", item.getItemClass());
            itemObject.put("power", item.getPower());
            itemObject.put("durability", item.getDurability());
            jsonArray.put(itemObject);
        }

        // Guardamos el JSON en el archivo
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(jsonArray.toString(4)); // Escribimos el JSON con formato indentado
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
